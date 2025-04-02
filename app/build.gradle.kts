plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.room)
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\Tomek\\keystore\\repo.jks")
            storePassword = "tomproj!@123"
            keyAlias = "keyDev"
            keyPassword = "dev!@123_"
        }
    }
    namespace = "com.packt.pets"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.packt.pets"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.packt.pets.InstrumentationTestRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
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
    }
    sourceSets {
        getByName("main") {
            resources {
                srcDirs("src\\main\\resources", "src\\test\\resources")
            }
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.adaptive)
    testImplementation(libs.junit)
    testImplementation(libs.bundles.tests)
    // androidTestImplementation(libs.androidx.activity.compose)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)
}