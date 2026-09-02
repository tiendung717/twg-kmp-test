import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        val ext = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

        ext.sourceSets.named("commonMain").configure {
            dependencies {
                implementation(libs.findLibrary("ktor-core").get())
                implementation(libs.findLibrary("ktor-serialization").get())
                implementation(libs.findLibrary("ktor-content-negotiation").get())
                implementation(libs.findLibrary("ktor-logging").get())
                implementation(libs.findLibrary("ktor-auth").get())
            }
        }

        ext.sourceSets.configureEach {
            val engine = when (name) {
                "androidMain" -> "ktor-okhttp"
                "iosMain" -> "ktor-darwin"
                "jvmMain" -> "ktor-java"
                else -> return@configureEach
            }
            dependencies {
                implementation(libs.findLibrary(engine).get())
            }
        }
    }
}
