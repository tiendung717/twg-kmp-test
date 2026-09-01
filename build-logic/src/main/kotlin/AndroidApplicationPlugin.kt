import com.android.build.api.dsl.ApplicationExtension
import configure.AndroidProjectConfig
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        setProjectConfig(project)
        setVersionConfig(project)
        setSigningConfig(project)
    }

    private fun applyPlugins(project: Project) {
        with(project.pluginManager) {
            apply("com.android.application")
        }
    }

    private fun setProjectConfig(project: Project) {
        project.plugins.withId("com.android.application") {
            project.extensions.configure<ApplicationExtension>("android") {
                defaultConfig {
                    minSdk = AndroidProjectConfig.MIN_SDK
                    targetSdk = AndroidProjectConfig.TARGET_SDK
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_21
                    targetCompatibility = JavaVersion.VERSION_21
                }

                buildFeatures {
                    buildConfig = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        excludes += "META-INF/DEPENDENCIES"
                    }
                }
            }
        }
    }

    private fun setSigningConfig(project: Project) {
        project.plugins.withId("com.android.application") {
            val props = Properties()
            val keystoreFile = project.rootProject.file("keystore.properties")
            if (keystoreFile.exists()) {
                props.load(FileInputStream(keystoreFile))
            } else {
                throw GradleException("Missing keystore.properties file!")
            }

            project.extensions.configure<ApplicationExtension>("android") {
                signingConfigs {
                    create("sign_debug") {
                        storeFile = project.rootProject.file(props["debugStoreFile"].toString())
                        storePassword = props["debugStorePassword"].toString()
                        keyAlias = props["debugKeyAlias"].toString()
                        keyPassword = props["debugKeyPassword"].toString()
                    }

                    create("sign_release") {
                        storeFile = project.rootProject.file(props["releaseStoreFile"].toString())
                        storePassword = props["releaseStorePassword"].toString()
                        keyAlias = props["releaseKeyAlias"].toString()
                        keyPassword = props["releaseKeyPassword"].toString()
                    }
                }

                buildTypes {
                    getByName("debug") {
                        signingConfig = signingConfigs.getByName("sign_debug")
                    }

                    getByName("release") {
                        signingConfig = signingConfigs.getByName("sign_release")
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }

            project.logger.lifecycle("🔐 [Android] Signing configs applied from keystore.properties")
        }
    }

    private fun setVersionConfig(project: Project) {
        project.plugins.withId("com.android.application") {
            val props = Properties()
            val versionFile = project.rootProject.file("version.properties")

            if (versionFile.exists()) {
                props.load(FileInputStream(versionFile))
            } else {
                throw GradleException("Missing version.properties file!")
            }

            val major = props["VERSION_MAJOR"].toString().toInt()
            val minor = props["VERSION_MINOR"].toString().toInt()
            val patch = props["VERSION_PATCH"].toString().toInt()

            val versionCode = major * 10000 + minor * 100 + patch
            val versionName = "$major.$minor.$patch"

            project.extensions.configure<ApplicationExtension>("android") {
                defaultConfig.apply {
                    this.versionName = versionName
                    this.versionCode = versionCode
                }
            }

            project.logger.lifecycle("📦 [Android] Applied versioning: versionCode=$versionCode, versionName=$versionName")
        }
    }
}
