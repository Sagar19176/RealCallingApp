plugins {
    // com.android.application must be applied first; it sets up the Android build model.
    alias(libs.plugins.android.application)

    // org.jetbrains.kotlin.android (Kotlin 2.0.21) – the canonical plugin ID.
    // Apply ONLY ONCE. Applying 'kotlin-android' and 'org.jetbrains.kotlin.android' together
    // would cause: "Cannot add extension with name 'kotlin', as there is an extension already registered."
    alias(libs.plugins.kotlin.android)

    // Kotlin Compose compiler plugin – required since Kotlin 2.x; must use the same
    // Kotlin version (2.0.21) as kotlin.android above.
    alias(libs.plugins.kotlin.compose)

    // KotlinX Serialization plugin – enables @Serializable annotation and JSON codec.
    alias(libs.plugins.kotlin.serialization)

    // KSP (Kotlin Symbol Processing) 2.0.21-1.0.27 – annotation processor for Room and Hilt.
    alias(libs.plugins.ksp)

    // Hilt Android Gradle plugin 2.52 – generates Hilt component and entry-point code.
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.oceanentp.realcalling"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.oceanentp.realcalling"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // kotlinOptions must match compileOptions; both set to JVM 11.
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)

    // Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // Compose BOM – manages versions for all androidx.compose.* artifacts below
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Compose Animation (standalone, version 1.10.3)
    implementation(libs.androidx.compose.animation)

    // Glance AppWidget – homescreen widget support (version 1.1.1)
    implementation(libs.androidx.glance.appwidget)

    // Navigation Compose (version 2.8.5)
    implementation(libs.androidx.navigation.compose)

    // Hilt Dependency Injection (version 2.52)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // Hilt + Navigation Compose integration
    implementation(libs.hilt.navigation.compose)

    // Room local database (version 2.6.1) – compiler via KSP
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // DataStore Preferences (version 1.1.1)
    implementation(libs.androidx.datastore.preferences)

    // KotlinX Serialization JSON (version 1.7.3)
    implementation(libs.kotlinx.serialization.json)

    // Unit tests
    testImplementation(libs.junit)

    // Instrumented tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug – Compose tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}