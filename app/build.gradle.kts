import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localProperties = gradleLocalProperties(rootDir, providers)

fun getLocalProp(key: String): String {
    return localProperties.getProperty(key)
        ?: throw GradleException("Missing '$key' in local.properties")
}

fun getLocalPropOrDefault(key: String, defaultValue: String): String {
    return localProperties.getProperty(key) ?: defaultValue
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
            signingConfig = signingConfigs.getByName("debug")
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
    val testAdmobAppId = "ca-app-pub-3940256099942544~3347511713"
    val testAdmobBannerUnitId = "ca-app-pub-3940256099942544/6300978111"
    val prodAdmobAppId = getLocalPropOrDefault(
        "ADMOB_APP_ID",
        testAdmobAppId
    )
    val prodAdmobBannerUnitId = getLocalPropOrDefault(
        "ADMOB_BANNER_UNIT_ID",
        testAdmobBannerUnitId
    )


    productFlavors {
        create("local") {
            dimension = "env"
            applicationIdSuffix = ".local"
            versionNameSuffix = "-local"
            resValue("string", "app_name", "Pompa Local")

            buildConfigField("String", "POMPA_BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "POMPA_EMULATOR_BASE_URL", "\"$emulatorBaseUrl\"")
            buildConfigField("String", "ADMOB_BANNER_UNIT_ID", "\"$testAdmobBannerUnitId\"")
            buildConfigField("Boolean", "IS_PROD", "false")
            resValue("string", "admob_app_id", testAdmobAppId)
        }

        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Pompa Dev")
            buildConfigField("String", "POMPA_BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "POMPA_EMULATOR_BASE_URL", "\"$emulatorBaseUrl\"")
            buildConfigField("String", "ADMOB_BANNER_UNIT_ID", "\"$testAdmobBannerUnitId\"")
            buildConfigField("Boolean", "IS_PROD", "false")
            resValue("string", "admob_app_id", testAdmobAppId)
        }

        create("prod") {
            dimension = "env"
            resValue("string", "app_name", "Pompa")
            buildConfigField("String", "POMPA_BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "POMPA_EMULATOR_BASE_URL", "\"$emulatorBaseUrl\"")
            buildConfigField("String", "ADMOB_BANNER_UNIT_ID", "\"$prodAdmobBannerUnitId\"")
            buildConfigField("Boolean", "IS_PROD", "true")
            resValue("string", "admob_app_id", prodAdmobAppId)
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
    implementation(libs.google.play.services.ads)

}
