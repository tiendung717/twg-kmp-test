import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class LoggingConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

        ext.sourceSets.named("commonMain").configure {
            dependencies {
                implementation(project(":base:logging"))
            }
        }
    }
}
