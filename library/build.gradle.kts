@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    `maven-publish`
}

group = "dev.icerock.compose"
version = "0.1.0"

android {
    namespace = "dev.icerock.compose.pbn"
    compileSdk = 33
    defaultConfig.minSdk = 21

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.4.3"

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(platform(libs.compose.bom))

    api(libs.ui)
    api(libs.material)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            artifactId = "popup-bottom-navigation"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
