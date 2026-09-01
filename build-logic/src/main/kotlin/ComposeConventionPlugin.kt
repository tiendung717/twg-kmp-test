import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
        project.pluginManager.apply("org.jetbrains.compose")
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        configureCompose(project, libs)

        project.logger.lifecycle("✅ ComposeConventionPlugin: Compose setup applied")
    }

    private fun configureCompose(project: Project, libs: VersionCatalog) {
        project.extensions.configure<KotlinMultiplatformExtension> {
            val compose = project.extensions.getByType(ComposeExtension::class)

            sourceSets.commonMain {
                dependencies {
                    implementation(compose.dependencies.runtime)
                    implementation(compose.dependencies.foundation)
                    implementation(compose.dependencies.material3)
                    implementation(compose.dependencies.materialIconsExtended)
                    implementation(compose.dependencies.ui)
                    implementation(compose.dependencies.animation)
                    implementation(compose.dependencies.uiUtil)
                    implementation(compose.dependencies.components.resources)
                    implementation(compose.dependencies.components.uiToolingPreview)

                    implementation(libs.findLibrary("compose-navigation").get())
                    implementation(libs.findLibrary("compose-lifecycle-viewmodel").get())
                }
            }

            sourceSets.androidMain {
                dependencies {
                    implementation(compose.dependencies.uiTooling)
                    implementation(compose.dependencies.preview)
                    implementation(libs.findLibrary("androidx-compose-activity").get())
                    implementation(libs.findLibrary("androidx-compose-google-fonts").get())
                }
            }
        }
    }
}
