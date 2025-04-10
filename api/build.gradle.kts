plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

val apiToken = System.getenv("SUPER_HERO_API_TOKEN") ?: ""

android {
    namespace = "com.shodo.android.api"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "apiToken", "\"${apiToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    android.buildFeatures.buildConfig = true
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    // Coroutines Core - Flow
    implementation(libs.kotlinx.coroutines.core)

    // Koin - Dependency injection
    implementation(libs.koin.core)

    // SquareUp Retrofit & OkHttp - Network
    implementation(libs.squareup.okhttp.brotli)
    implementation(libs.squareup.retrofit.converter.gson)
}