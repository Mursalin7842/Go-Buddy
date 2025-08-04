// FILE: build.gradle.kts (Project Level)
/*
 * This is the top-level build file where you can add configuration options
 * common to all sub-projects/modules. It defines the plugins used across
 * the entire project.
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // PRODUCTION: Add Hilt plugin
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}