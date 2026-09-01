import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import configure.AndroidProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class KotlinApplicationPlugin : Plugin<Project> {
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

    private fun setProjectConfig(project: Project) {
        project.plugins.withId("com.android.kotlin.multiplatform.library") {
            project.extensions.configure<KotlinMultiplatformExtension>("kotlin") {
                (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryExtension>("android") {
                    compileSdk = AndroidProjectConfig.COMPILE_SDK
                    minSdk = AndroidProjectConfig.MIN_SDK

                    withHostTest { }
                }
            }
        }

        project.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            project.extensions.configure<KotlinMultiplatformExtension>("kotlin") {
                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                        linkerOpts.add("-lsqlite3")
                    }
                }

                sourceSets.apply {
                    named { it.lowercase().startsWith("ios") }.configureEach {
                        languageSettings {
                            optIn("kotlinx.cinterop.ExperimentalForeignApi")
                        }
                    }
                    commonMain {
                        sourceSets.named("commonMain").configure {
                            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                        }
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
