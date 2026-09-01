import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import configure.AndroidProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        setProjectConfig(project)
        configureKspTaskOrdering(project)
    }

    private fun applyPlugins(project: Project) {
        with(project.pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
            apply("com.android.kotlin.multiplatform.library")
        }
    }

    private val Project.iosFrameworkName: String
        get() = path.removePrefix(":").replace(':', '-') + "-kit"

    private fun setProjectConfig(project: Project) {
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        project.plugins.withId("com.android.kotlin.multiplatform.library") {
            project.extensions.configure<KotlinMultiplatformExtension>("kotlin") {
                (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryExtension>("android") {
                    compileSdk = AndroidProjectConfig.COMPILE_SDK
                    minSdk = AndroidProjectConfig.MIN_SDK

                    withHostTest { }
                }
            }
        }

        project.extensions.configure<KotlinMultiplatformExtension>("kotlin") {
            val xcfName = project.iosFrameworkName

            iosArm64 {
                binaries.framework {
                    baseName = xcfName
                }
            }

            iosSimulatorArm64 {
                binaries.framework {
                    baseName = xcfName
                }
            }

            sourceSets.apply {
                named { it.lowercase().startsWith("ios") }.configureEach {
                    languageSettings {
                        optIn("kotlinx.cinterop.ExperimentalForeignApi")
                    }
                }
                named("commonMain").configure {
                    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                }

                named("commonTest").configure {
                    dependencies {
                        implementation(libs.findLibrary("kotlin-test").get())
                    }
                }
            }
        }
    }

    private fun configureKspTaskOrdering(project: Project) {
        project.plugins.withId("com.google.devtools.ksp") {
            project.tasks.configureEach {
                if (name != "kspCommonMainKotlinMetadata" &&
                    (name.startsWith("ksp") || this is KotlinCompilationTask<*>)) {
                    dependsOn("kspCommonMainKotlinMetadata")
                }
            }
        }
    }
}
