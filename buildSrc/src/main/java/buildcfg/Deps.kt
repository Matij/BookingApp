package buildcfg

import org.gradle.api.artifacts.dsl.DependencyHandler

const val kotlinVersion = "1.6.10"

object appConfig {
    const val applicationId = "com.martafoderaro.bookingapp"

    const val compileSdk = 31
    const val buildTools = "31.0.0"

    const val minSdk = 26
    const val targetSdk = 31
    const val versionCode = 1
    const val versionName = "1.0"
}

object Deps {
    const val timber = "com.jakewharton.timber:timber:5.0.1"

    object AndroidX {
        const val activityCompose = "androidx.activity:activity-compose:1.4.0"
        const val appCompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.4.1"
        const val pagingCompose = "androidx.paging:paging-compose:1.0.0-alpha14"
        const val process = "androidx.lifecycle:lifecycle-process:2.4.1"
    }

    object Coil {
        const val compose = "io.coil-kt:coil-compose:2.0.0"
    }

    object Compose {
        const val version = "1.1.1"

        const val compiler = "androidx.compose.compiler:compiler:$version"
        const val pager = "com.google.accompanist:accompanist-pager:0.23.1"
        const val pagerIndicators = "com.google.accompanist:accompanist-pager-indicators:0.23.1"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val layout = "androidx.compose.foundation:foundation-layout:$version"
        const val material = "androidx.compose.material:material:$version"
        const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val ui = "androidx.compose.ui:ui:$version"
    }

    object Calendar {
        const val calendar = "com.github.sundeepk:compact-calendar-view:3.0.0"
    }

    object Coroutines {
        private const val version = "1.6.1"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Desugar {
        const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:1.1.5"
    }

    object Hilt {
        private const val version = "2.38.1"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$version"
        const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object Kizitonwose {
        const val calendarView = "com.github.kizitonwose:CalendarView:1.0.3"
    }

    object Kotlin {
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
    }

    object Lifecycle {
        private const val version = "2.4.0"

        const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha05"
    }

    object Material {
        const val material = "com.google.android.material:material:1.6.0"
    }

    object Moshi {
        private const val version = "1.13.0"
        const val moshi = "com.squareup.moshi:moshi:$version"
        const val moshiAdapters = "com.squareup.moshi:moshi-adapters:$version"
        const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:$version"
        const val moshiKotlinCodegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object OkHttp {
        private const val version = "4.9.3"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        private const val versionConverters = "2.8.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofitConverters = "com.squareup.retrofit2:retrofit-converters:$versionConverters"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object Picasso {
        const val version = "2.71828"
        const val picasso = "com.squareup.picasso:picasso:$version"
    }

    object Room {
        private const val version = "2.4.2"
        const val room = "androidx.room:room-runtime:$version"
        const val roomKtx = "androidx.room:room-ktx:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val testing = "androidx.room:room-testing:$version"
    }

    object Test {
        const val coreTesting = "android.arch.core:core-testing:1.1.1"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        const val extJUnit = "androidx.test.ext:junit:1.1.3"

        const val jUnit = "junit:junit:4.13.2"
        const val kotlinJUnit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
        const val mockk = "io.mockk:mockk:1.12.4"
    }

    fun DependencyHandler.implementationCompose() {
        arrayOf(
            AndroidX.activityCompose,
            AndroidX.pagingCompose,
            Lifecycle.viewModelCompose,
            Compose.compiler,
            Compose.layout,
            Compose.foundation,
            Compose.ui,
            Compose.material,
            Compose.materialIconsExtended,
            Compose.runtime,
        ).forEach { add("implementation", it) }

        add("debugImplementation", Compose.tooling)
        add("debugImplementation", Kotlin.reflect)
    }

    fun DependencyHandler.addUnitTest(testImplementation: Boolean = true) {
        val configName = if (testImplementation) "testImplementation" else "implementation"

        add(configName, Coroutines.test)
        add(configName, Test.coreTesting)
        add(configName, Test.espresso)
        add(configName, Test.extJUnit)
        add(configName, Test.jUnit)
        add(configName, Test.kotlinJUnit)
        add(configName, Test.mockk)
    }
}
