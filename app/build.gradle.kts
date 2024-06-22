import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
}

val credentialFile = rootProject.file("credentials.properties")
val credentialProperty = Properties()
credentialFile.inputStream().use {
    credentialProperty.load(it)
}

android {
    namespace = "ru.elocont.locationbeaconsandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.elocont.locationbeaconsandroid"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField(
            "String", "OPENCELLID_API_KEY",
            credentialProperty["OPENCELLID_API_KEY"].toString()
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.lifecycle.vewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.extensions)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.moshi)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    ksp(libs.moshi.compiler)
}