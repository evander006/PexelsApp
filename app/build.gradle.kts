plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.pexels"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pexels"
        minSdk = 25
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
    hilt {
        enableAggregatingTask = false
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // SplashScreen & Nav
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.navigation:navigation-compose:2.9.4")
    // Retrofit + Serialization
    implementation("com.squareup.retrofit2:retrofit:2.3.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.57")
    kapt("com.google.dagger:hilt-android-compiler:2.57")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Room
    val room_version = "2.8.0"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // WorkManager + Hilt
    implementation("androidx.work:work-runtime-ktx:2.10.4")
    implementation("androidx.hilt:hilt-work:1.3.0")
    //paging3
    implementation("androidx.paging:paging-runtime:3.3.6")
    implementation("androidx.paging:paging-compose:3.3.6")
}
