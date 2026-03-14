import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localProperties = gradleLocalProperties(rootDir, providers)

fun getLocalProp(key: String): String {
    return localProperties.getProperty(key)
        ?: throw GradleException("Missing '$key' in local.properties")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secrets)
    alias(libs.plugins.serialization)
    id("kotlin-parcelize")
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.pompa.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pompa.android"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    flavorDimensions += "env"

    val baseUrl = getLocalProp("POMPA_LOCAL_BASE_URL")
    val emulatorBaseUrl = getLocalProp("POMPA_LOCAL_EMULATOR_BASE_URL")


    productFlavors {
        create("local") {
            dimension = "env"
            applicationIdSuffix = ".local"
            versionNameSuffix = "-local"
            resValue("string", "app_name", "Pompa Local")

            buildConfigField("String", "POMPA_BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "POMPA_EMULATOR_BASE_URL", "\"$emulatorBaseUrl\"")
            buildConfigField("Boolean", "IS_PROD", "false")
        }

        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Pompa Dev")
            buildConfigField("String", "POMPA_BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "POMPA_EMULATOR_BASE_URL", "\"$emulatorBaseUrl\"")
            buildConfigField("Boolean", "IS_PROD", "false")
        }

        create("prod") {
            dimension = "env"
            resValue("string", "app_name", "Pompa")
            buildConfigField("String", "POMPA_BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "POMPA_EMULATOR_BASE_URL", "\"$emulatorBaseUrl\"")
            buildConfigField("Boolean", "IS_PROD", "true")
        }
    }
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.constriantlayout.compose)
    implementation(libs.coil.svg)
    implementation(libs.androidx.compose.material.navigation)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.airbnb.android.lottie)

    implementation(libs.squareup.moshi)
    implementation(libs.squareup.retrofit.moshi.converter)
    ksp(libs.squareup.moshi.codegen)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)

}
