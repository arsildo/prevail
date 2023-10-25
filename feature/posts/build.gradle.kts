@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.arsildo.feature.posts"
    compileSdk = 34
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =  libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":feature:boards"))

    implementation(project(":core:network"))
    implementation(project(":core:media"))
    implementation(project(":core:preferences"))
    implementation(project(":core:model"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coil)
    implementation(libs.datastore.preferences)
    implementation(libs.bundles.retrofit)
}