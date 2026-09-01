import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.api.artifacts.VersionCatalogsExtension

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        if (!project.plugins.hasPlugin("com.google.devtools.ksp")) {
            project.pluginManager.apply("com.google.devtools.ksp")
        }

        configureDependencies(project, libs)

        project.logger.lifecycle("✅ KoinConventionPlugin: Koin + ksp setup applied")
    }

    private fun configureDependencies(project: Project, libs: VersionCatalog) {
        project.extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension::class.java) {
            sourceSets.commonMain.dependencies {
                implementation(libs.findLibrary("koin-core").get())
                implementation(libs.findLibrary("koin-compose-viewmodel").get())
                api(libs.findLibrary("koin-annotation").get())
            }

            sourceSets.androidMain.dependencies {
                implementation(libs.findLibrary("koin-android").get())
                implementation(libs.findLibrary("koin-compose").get())
                implementation(libs.findLibrary("koin-androidx-workmanager").get())
                project.dependencies.add("ksp", libs.findLibrary("koin-ksp").get())
            }
        }

        project.dependencies {
            add("kspCommonMainMetadata", libs.findLibrary("koin-ksp").get())
            add("kspAndroid", libs.findLibrary("koin-ksp").get())
            add("kspIosArm64", libs.findLibrary("koin-ksp").get())
            add("kspIosSimulatorArm64", libs.findLibrary("koin-ksp").get())
        }
    }
}
