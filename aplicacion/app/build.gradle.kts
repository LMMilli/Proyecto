plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.aplicacion"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.aplicacion"
        minSdk = 26
        targetSdk = 36
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Retrofit para las peticiones HTTP
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    //Gson para traducir los JSON del servidor
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    //Libreria para dibujar graficas
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
}