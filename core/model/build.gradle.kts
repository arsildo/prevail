@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.arsildo.core.model"
    compileSdk = 34
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.kotlinx.serialization.json)
}