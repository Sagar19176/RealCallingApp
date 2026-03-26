// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Declare plugins here with 'apply false' so Gradle resolves and caches their versions
    // for all subprojects without actually applying them at the root level.
    // Each plugin is applied only in the module(s) that need it (e.g., app/build.gradle.kts).

    // AGP 8.11.2 – coordinated with Kotlin 2.0.21 for Compose compatibility.
    alias(libs.plugins.android.application) apply false

    // org.jetbrains.kotlin.android (Kotlin 2.0.21) – the canonical plugin ID.
    // NOTE: Do NOT add a duplicate 'kotlin-android' entry here. Both names resolve to
    // the same plugin; applying it twice causes:
    //   "Cannot add extension with name 'kotlin', as there is an extension already registered."
    alias(libs.plugins.kotlin.android) apply false

    // Kotlin Compose compiler plugin – required since Kotlin 2.x; must use the same
    // Kotlin version (2.0.21) as kotlin.android above.
    alias(libs.plugins.kotlin.compose) apply false

    // KotlinX Serialization plugin (Kotlin 2.0.21) – enables @Serializable and JSON support.
    alias(libs.plugins.kotlin.serialization) apply false

    // KSP 2.0.21-1.0.27 – Kotlin Symbol Processing, used by Room and Hilt annotation processors.
    alias(libs.plugins.ksp) apply false

    // Hilt Android Gradle plugin 2.52 – required for Hilt dependency injection code generation.
    alias(libs.plugins.hilt.android) apply false
}