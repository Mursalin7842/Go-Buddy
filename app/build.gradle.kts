// =================================================================================
// 📁 APP MODULE (/app)
// =================================================================================

// FILE: app/build.gradle.kts (Module Level)
/*
 * This build file is for the ':app' module. It includes Android-specific
 * configurations and declares the dependencies for the application.
 */
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // PRODUCTION: Add Hilt and Kapt plugins
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "mursalin.companion.gobuddy"
    compileSdk = 36

    defaultConfig {
        applicationId = "mursalin.companion.gobuddy"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.appwrite)
    implementation(libs.coil.compose)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // PRODUCTION: Add Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Import the OkHttp BOM
    implementation(platform(libs.okhttp.bom)) // Or the latest version

    // Now, you can declare OkHttp dependencies without specifying versions
    // The BOM will manage their versions.
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
}

