plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    id("kotlin-kapt")
}

android {
    namespace = "dev.mikhalchenkov.isxcatalogviewer.core.data"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    implementation(libs.androidx.datastore)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
}