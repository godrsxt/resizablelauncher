plugins {
    id("com.android.application") version "8.7.0"
    id("org.jetbrains.kotlin.android") version "2.0.0"
}

android {
    namespace = "com.example.resizablelauncher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.resizablelauncher"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
}
