plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // Cargando versión unificada correctamente
}

android {
    namespace = "com.example.vinilotfg"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.vinilotfg"
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

    // Actualizamos a Java 17 para soportar Kotlin 2.0.21 y SDK 36
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
    }

    buildFeatures {
        compose = true
    }

    // Al usar Kotlin 2.x con el plugin 'kotlin.compose', ya NO necesitas
    // forzar la propiedad kotlinCompilerExtensionVersion aquí.
    // El propio compilador de Kotlin gestiona Compose internamente de forma automática.
}

dependencies {
    // Android Core y UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // UI Adicional
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- CONEXIÓN API ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3") // Esencial para Supabase

    // --- GESTIÓN DE DATOS (ViewModel + Corrutinas) ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // --- IMÁGENES (Coil) ---
    implementation(libs.coil.compose)

    // --- SUPABASE COMPLETO (Estructura oficial unificada) ---
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.5.2")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.5.2") // Necesario para la inicialización del cliente core
    implementation("io.ktor:ktor-client-android:2.3.11") // O la versión que use tu proyecto

    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}