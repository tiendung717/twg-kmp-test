import configure.AndroidProjectConfig

plugins {
    id("kotlin.application.convention.plugin")
    id("compose.convention.plugin")
    id("kotlinx.convention.plugin")
    id("koin.convention.plugin")
    id("logging.convention.plugin")
}

compose.resources {
    packageOfResClass = "nz.co.warehouseandroidtest.shared.resources"
    generateResClass = always
}

kotlin {
    android {
        namespace = "nz.co.warehouseandroidtest.shared"
        compileSdk { version = release(AndroidProjectConfig.COMPILE_SDK) }
        androidResources.enable = true
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":base:designsystem"))
            implementation(project(":base:logging"))
            implementation(project(":base:navigation"))
            implementation(project(":data"))
            implementation(project(":feature:search"))
            implementation(project(":feature:product"))

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies { }
        iosMain.dependencies { }
    }
}
