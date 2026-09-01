plugins {
    id("kotlin.library.convention.plugin")
    id("koin.convention.plugin")
}

kotlin {
    android {
        namespace = "nz.co.warehouseandroidtest.logging"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kermit)
            implementation(libs.kermit.crashlytics)
        }
    }
}
