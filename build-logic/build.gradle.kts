import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("kotlin-application") {
            id = "kotlin.application.convention.plugin"
            implementationClass = "KotlinApplicationPlugin"
        }

        create("android-application") {
            id = "android.application.convention.plugin"
            implementationClass = "AndroidApplicationPlugin"
        }

        create("compose") {
            id = "compose.convention.plugin"
            implementationClass = "ComposeConventionPlugin"
        }

        create("koin") {
            id = "koin.convention.plugin"
            implementationClass = "KoinConventionPlugin"
        }

        create("kotlinx") {
            id = "kotlinx.convention.plugin"
            implementationClass = "KotlinxConventionPlugin"
        }

        create("ktor") {
            id = "ktor.convention.plugin"
            implementationClass = "KtorConventionPlugin"
        }

        create("logging") {
            id = "logging.convention.plugin"
            implementationClass = "LoggingConventionPlugin"
        }

        create("library") {
            id = "kotlin.library.convention.plugin"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
}
