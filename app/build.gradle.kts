plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "id.faradyna.miniedcapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "id.faradyna.miniedcapp"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    with(libs) {
        implementation(platform(androidx.compose.bom))
        implementation(bundles.compose)

        implementation(androidx.activity.compose)
        implementation(androidx.core.ktx)
        implementation(androidx.navigation.compose)

        testImplementation(junit)
        testImplementation(mockk)
        testImplementation(kotlinx.coroutines.test)
        androidTestImplementation(platform(androidx.compose.bom))
        androidTestImplementation(androidx.compose.ui.test.junit4)
        androidTestImplementation(androidx.espresso.core)
        androidTestImplementation(androidx.junit)
        debugImplementation(androidx.compose.ui.test.manifest)
        debugImplementation(androidx.compose.ui.tooling)

        // Hilt
        implementation(hilt.android)
        ksp(hilt.compiler)
        implementation(androidx.hilt.navigation.compose)

        // Coroutines
        implementation(kotlinx.coroutines.android)

        // Lifecycle
        implementation(bundles.lifecycle)

        // Datastore
        implementation(androidx.datastore.preferences)

        // Room
        implementation(bundles.room)
        ksp(androidx.room.compiler)
    }
}