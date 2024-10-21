plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.smartmobilityx"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smartmobilityx"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.journeyapps.zxing) // QR Code Scanner
    implementation(libs.play.services.maps) // Google Maps
    implementation(libs.dexter)
    implementation(libs.google.firebase.auth) // firebase Authentication
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")
    implementation(libs.firebase.database) // firebase RealtimeDatabase
    implementation(libs.play.services.maps)
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // paymongo
    implementation ("com.google.firebase:firebase-storage:20.2.1") // firebase storage
    implementation("com.google.android.gms:play-services-location:21.0.1") //gmaps current location

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}