plugins {
    id("kotlin.library.convention.plugin")
    id("compose.convention.plugin")
    id("kotlinx.convention.plugin")
    id("koin.convention.plugin")
}

compose.resources {
    packageOfResClass = "nz.co.warehouseandroidtest.feature.product.resources"
    generateResClass = always
}

kotlin {
    android {
        namespace = "nz.co.warehouseandroidtest.feature.product"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":data"))
            implementation(project(":base:common"))
            implementation(project(":base:designsystem"))
            implementation(project(":base:navigation"))

            implementation(libs.coil.compose)
        }
        commonTest.dependencies {
            implementation(libs.ktor.core)
            implementation(libs.ktor.mock)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
