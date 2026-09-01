plugins {
    id("kotlin.library.convention.plugin")
    id("ktor.convention.plugin")
    id("kotlinx.convention.plugin")
    id("koin.convention.plugin")
    id("logging.convention.plugin")
}

kotlin {
    android {
        namespace = "nz.co.warehouseandroidtest.data"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore.preferences)
            implementation(libs.okio)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
