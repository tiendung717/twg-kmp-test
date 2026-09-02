import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinxConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        project.pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        project.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            project.extensions.getByType<KotlinMultiplatformExtension>().apply {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(libs.findLibrary("kotlinx-datetime").get())
                    implementation(libs.findLibrary("kotlinx-serialization-json").get())
                    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                }
            }
        }
    }
}
