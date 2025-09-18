plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt) // Hilt plugin alias
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.10"

}

android {
    namespace = "com.example.tiffinapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tiffinapp"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-core:1.0.0") //
    implementation ("androidx.compose.material:material-icons-extended:1.6.1")

    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation ("com.google.accompanist:accompanist-pager:0.28.0")

    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation ("com.razorpay:checkout:1.6.40")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // If using Retrofit + Kotlinx Serialization Converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")



    // Krossbow STOMP with Ktor WebSocket (Android compatible)
    implementation ("org.hildan.krossbow:krossbow-stomp-core:7.0.0")
    implementation ("org.hildan.krossbow:krossbow-websocket-ktor:7.0.0")
    implementation ("com.airbnb.android:lottie-compose:6.3.0")

    // Google Play Services Location
    implementation ("com.google.android.gms:play-services-location:21.0.1")





    //material3 ui
    implementation("androidx.compose.material3.adaptive:adaptive:1.2.0-alpha07")
    implementation ("androidx.compose.material3.adaptive:adaptive-layout:1.2.0-alpha07")
    implementation ("androidx.compose.material3.adaptive:adaptive-navigation:1.2.0-alpha07")




}