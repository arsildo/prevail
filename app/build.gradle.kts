@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.arsildo.prevail"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arsildo.prevail"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = true
    }
}

dependencies {

    implementation(project(":feature:threadcatalog"))
    implementation(project(":feature:preferences"))

    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":core:preferences"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.bundles.koin)
    ksp(libs.koin.ksp)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.coil)
    implementation(libs.datastore.preferences)
    implementation(libs.bundles.retrofit)

}