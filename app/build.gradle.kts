import buildcfg.*
import buildcfg.Deps.addUnitTest

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = appConfig.compileSdk

    defaultConfig {
        applicationId = appConfig.applicationId
        minSdk = appConfig.minSdk
        targetSdk = appConfig.targetSdk
        versionCode = appConfig.versionCode
        versionName = appConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes.all {
        buildConfigField("String", "REST_CAT_API_BASE_URL", "\"https://api.thecatapi.com/v1/\"")
        buildConfigField("String", "REST_CAT_API_KEY", "\"0d627083-3eb0-4da7-b38e-1eba46662a91\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Deps.Compose.version
    }
}

dependencies {
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.fragmentKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.process)
    implementation(Deps.Material.material)
    coreLibraryDesugaring (Deps.Desugar.desugarJdkLibs)
    implementation(Deps.Kizitonwose.calendarView)
    implementation(Deps.Picasso.picasso)

    // DI
    implementation(Deps.Hilt.hiltAndroid)
    kapt(Deps.Hilt.hiltCompiler)

    // Networking
    implementation(Deps.OkHttp.okhttp)
    implementation(Deps.OkHttp.loggingInterceptor)
    implementation(Deps.Retrofit.retrofit)
    implementation(Deps.Retrofit.retrofitConverters)
    implementation(Deps.Retrofit.converterMoshi)
    implementation(Deps.Moshi.moshi)
    implementation(Deps.Moshi.moshiAdapters)
    implementation(Deps.Moshi.moshiKotlin)
    kapt(Deps.Moshi.moshiKotlinCodegen)

    // Database
    implementation(Deps.Room.room)
    implementation(Deps.Room.roomKtx)
    kapt(Deps.Room.compiler)

    // Coroutines
    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)

    // Image loading
    implementation(Deps.Coil.compose)

    // Pager
    implementation(Deps.Compose.pager)
    implementation(Deps.Compose.pagerIndicators)

    // Logging
    implementation(Deps.timber)

    // Test
    addUnitTest()
}