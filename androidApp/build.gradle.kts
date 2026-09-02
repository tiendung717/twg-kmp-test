import configure.AndroidProjectConfig

plugins {
    id("android.application.convention.plugin")
    alias(libs.plugins.compose.plugin)
}

android {
    namespace = AndroidProjectConfig.APPLICATION_ID
    compileSdk = AndroidProjectConfig.COMPILE_SDK
    defaultConfig {
        applicationId = AndroidProjectConfig.APPLICATION_ID
        minSdk = AndroidProjectConfig.MIN_SDK
        targetSdk = AndroidProjectConfig.TARGET_SDK
    }
}

dependencies {
    implementation(project(":app"))

    implementation(libs.androidx.compose.activity)
    implementation(libs.koin.android)
}
