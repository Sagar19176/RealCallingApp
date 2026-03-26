// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Declare plugins here with 'apply false' so Gradle resolves and caches their versions
    // for all subprojects without actually applying them at the root level.
    // Each plugin is applied only in the module(s) that need it (e.g., app/build.gradle.kts).
    alias(libs.plugins.android.application) apply false
    // NOTE: 'kotlin-android' is just a short alias for 'org.jetbrains.kotlin.android' – they
    // resolve to the same plugin. Applying either one a second time (via another alias or by
    // referencing the plugin ID directly) would try to re-register the 'kotlin' extension and
    // cause the build error:
    //   "Cannot add extension with name 'kotlin', as there is an extension already registered with that name."
    alias(libs.plugins.kotlin.android) apply false
    // Required since Kotlin 2.x for Compose support; must use the same Kotlin version as above.
    alias(libs.plugins.kotlin.compose) apply false
}