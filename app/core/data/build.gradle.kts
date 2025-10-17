plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization) apply false
}

android {
    namespace = "dev.mikhalchenkov.isxcatalogviewer.core.data"
    compileSdk = 36
}

dependencies {
    implementation(project(":app:core:domain"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    testImplementation(libs.test.mockk)
}