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

    // Koin
    implementation(libs.koin.core)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.brotli)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}