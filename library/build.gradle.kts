@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "dev.icerock.compose.bnn"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.4.3"
}

dependencies {
    implementation(platform(libs.compose.bom))
    api(libs.ui)
    api(libs.material)
}
