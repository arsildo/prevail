@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.arsildo.core.preferences"
    compileSdk = 34
}

dependencies {
    implementation(libs.bundles.koin)
    ksp(libs.koin.ksp)
    implementation(libs.datastore.preferences)
}