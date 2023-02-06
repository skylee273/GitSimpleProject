object Kotlin {
    const val standardLibrary = "1.4.10"
    const val coroutines = "1.3.9"
}

object AndroidSdk {
    const val min = 21
    private const val compile = 32
    const val target = compile
}

object AndroidClient {
    const val appId = "com.example.gitsimpleproject"
    const val versionCode = 1
    const val versionName = "1.0"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "4.1.0"
        const val gradleVersion = "6.6.1"
    }
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.standardLibrary}"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
}

object ScriptPlugins {
    const val infrastructure = "scripts.infrastructure"
    const val variants = "scripts.variants"
    const val quality = "scripts.quality"
    const val compilation = "scripts.compilation"
}

object Libraries {
    private object Versions {
        const val appCompat = "1.5.1"
        const val constraintLayout = "2.0.4"
        const val recyclerView = "1.1.0"
        const val cardView = "1.0.0"
        const val material = "1.7.0"
        const val lifecycle = "2.2.0"
        const val lifecycleExtensions = "2.1.0"
        const val annotations = "1.1.0"
        const val ktx = "1.3.2"
        const val glide = "4.11.0"
        const val retrofit = "2.9.0"
        const val okHttpLoggingInterceptor = "4.9.0"
        const val browser = "1.4.0"
        //TODO: Legacy -> WIP -> Try Hilt?
        const val dagger = "2.11"
        const val javaxAnnotations = "1.0"
        const val javaxInject = "1"
    }

    const val kotlinStdLib             = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Kotlin.standardLibrary}"
    const val kotlinCoroutines         = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Kotlin.coroutines}"
    const val kotlinCoroutinesAndroid  = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Kotlin.coroutines}"
    const val appCompat                = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout         = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val ktxCore                  = "androidx.core:core-ktx:${Versions.ktx}"
    const val lifecycleCompiler        = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val viewModel                = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val liveData                 = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleExtensions      = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val cardView                 = "androidx.cardview:cardview:${Versions.cardView}"
    const val recyclerView             = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val material                 = "com.google.android.material:material:${Versions.material}"
    const val androidAnnotations       = "androidx.annotation:annotation:${Versions.annotations}"
    const val glide                    = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val dagger                   = "com.google.dagger:dagger:${Versions.dagger}"
    const val retrofit                 = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitAdapter          = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLoggingInterceptor}"
    const val browser                  = "androidx.browser:browser:${Versions.browser}"
    //TODO: Legacy -> WIP -> Migrate to Koin or Hilt?
    const val daggerCompiler   = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val javaxAnnotation  = "javax.annotation:jsr250-api:${Versions.javaxAnnotations}"
    const val javaxInject      = "javax.inject:javax.inject:${Versions.javaxInject}"
}

object TestLibraries {
    private object Versions {
        const val junit4 = "4.13.2'"
        const val mockk = "1.10.0"
        const val robolectric = "4.4"
        const val kluent = "1.14"
        const val testRunner = "1.1.0"
        const val espressoCore = "3.5.0"
        const val espressoIntents = "3.1.0"
        const val testExtensions = "1.1.4"
        const val testRules = "1.1.0"
    }

    const val junit4          = "junit:junit:${Versions.junit4}"
    const val mockk           = "io.mockk:mockk:${Versions.mockk}"
    const val robolectric     = "org.robolectric:robolectric:${Versions.robolectric}"
    const val kluent          = "org.amshove.kluent:kluent:${Versions.kluent}"
    const val testRunner      = "androidx.test:runner:${Versions.testRunner}"
    const val testRules       = "androidx.test:rules:${Versions.testRules}"
    const val espressoCore    = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espressoIntents}"
    const val testExtJunit    = "androidx.test.ext:junit:${Versions.testExtensions}"
}