import java.util.Properties // <-- FIX #1: ADD THIS IMPORT AT THE TOP

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // THIS IS THE CORRECT LINE - IT DOESN'T SPECIFY A VERSION
    alias(libs.plugins.compose.compiler)
}
fun getApiKey(property: String): String {
    // FIX #1 (continued): Use the imported class directly
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty(property)
}


android {
    namespace = "com.example.newsapp"
    // Use compileSdk = 34, as 36 is likely a preview/unstable version
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 24
        // Use targetSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // --- FIX #2: ADD THIS CRITICAL LINE TO CREATE THE BUILDCONFIG VARIABLE ---
        buildConfigField("String", "NEWS_API_KEY", "\"${getApiKey("NEWS_API_KEY")}\"")
        buildConfigField("String", "MAPS_API_KEY", "\"${getApiKey("MAPS_API_KEY")}\"")
        manifestPlaceholders["MAPS_API_KEY"] = getApiKey("MAPS_API_KEY")
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
        sourceCompatibility = JavaVersion.VERSION_1_8 // Using 1.8 is more common and safer
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8" // Match this with compileOptions
    }
    buildFeatures {
        compose = true
        // Add this line to enable buildConfig
        buildConfig = true
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
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0") // Use 2.5.0 or a recent version

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // For SavedStateHandle in ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")

    // Add these for Google Maps
    implementation("com.google.maps.android:maps-compose:4.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

}