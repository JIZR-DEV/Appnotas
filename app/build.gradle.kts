plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    kotlin("kapt")

}

android {
    namespace = "com.unison.appproductos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.unison.appproductos"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        kotlinCompilerExtensionVersion = "1.6.0"  // Actualiza a la versión más reciente
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.camera.core)
    val nav_version = "2.8.0"

    // Jetpack Compose Core Libraries
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.foundation:foundation:1.5.0")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.compose.ui:ui-tooling:1.5.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.0")
    implementation ("io.coil-kt:coil-compose:2.2.2")

    implementation("androidx.compose.animation:animation:1.5.0")

    // Navegación en Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")


    // Accompanist SwipeRefresh para Pull to Refresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.30.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

// To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")

// optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

// optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

// optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

// optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

// optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")
    // iconos
    implementation ("androidx.compose.material:material-icons-extended:1.5.1")


// optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")
    // Otras dependencias necesarias
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    // Dependencias de prueba
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
