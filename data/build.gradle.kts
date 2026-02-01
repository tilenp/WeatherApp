plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "com.example.data"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "OPEN_WEATHER_MAP_API_KEY",
            "\"${properties["OPEN_WEATHER_MAP_API_KEY"]}\""
        )
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

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    /** project modules **/
    implementation(project(":domain"))

    /** Coroutines **/
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.kotlinx.coroutines.test)

    /** Hilt **/
    implementation(libs.com.google.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.android.compiler)

    /** MockK **/
    testImplementation(libs.io.mockk.mockk)
    testImplementation(libs.io.mockk.mockk.agent.jvm)

    /** MockWebServer **/
    testImplementation(libs.com.squareup.okhttp3.mockwebserver)

    /** OkHttp **/
    implementation(libs.com.squareup.okhttp3.logging.interceptor)

    /** Retrofit **/
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.gson)

    /** Jupiter **/
    testImplementation(libs.org.junit.jupiter.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kapt {
    correctErrorTypes = true
}