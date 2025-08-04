// =================================================================================
// 📁 PROJECT CONFIGURATION FILES
// =================================================================================

// FILE: settings.gradle.kts
/*
 * This file includes the sub-projects (modules) to be included in the build.
 * For this project, we only have the ':app' module. It also configures the
 * repositories for plugins.
 */
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Go Buddy"
include(":app")
