plugins {
    // com.android.application must be applied first; it sets up the Android build model.
    alias(libs.plugins.android.application)
    // org.jetbrains.kotlin.android (also available as the 'kotlin-android' short alias) –
    // registers the 'kotlin' DSL extension on the project.
    // Apply this plugin only ONCE. Applying the same plugin a second time – whether through
    // a different alias, by including 'org.jetbrains.kotlin.android' or 'kotlin-android' again,
    // or via a transitive application – causes the build error:
    //   "Cannot add extension with name 'kotlin', as there is an extension already registered with that name."
    alias(libs.plugins.kotlin.android)
    // org.jetbrains.kotlin.plugin.compose – required since Kotlin 2.x to enable the
    // Compose compiler plugin. This is separate from kotlin.android and must use the
    // same Kotlin version (see libs.versions.toml: kotlin).
    alias(libs.plugins.kotlin.compose)
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}