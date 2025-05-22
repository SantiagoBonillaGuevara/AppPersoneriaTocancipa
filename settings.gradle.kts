pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // Kotlin / Android
        id("org.jetbrains.kotlin.android")         version "1.8.21" apply false
        // Navigation Safe Args
        id("androidx.navigation.safeargs.kotlin")  version "2.7.0"  apply false
        // Google Services (Firebase, etc.)
        id("com.google.gms.google-services")       version "4.4.0"  apply false
    }
}

dependencyResolutionManagement {
    // prevent sub-projects from declaring their own repositories
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        // needed for libraries like MPAndroidChart, if fetched via JitPack
        maven("https://jitpack.io")
    }
}

rootProject.name = "Personería Tocancipá"
include(":app")
