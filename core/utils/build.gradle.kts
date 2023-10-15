@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.arsildo.core.utils"
    compileSdk = 34
}

dependencies {
    implementation(libs.core.ktx)
}