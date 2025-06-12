plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.sasaadamovic.brzevijesti"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sasaadamovic.brzevijesti"
        minSdk = 26
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.material)

    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.material:material:1.5.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("com.google.code.gson:gson:2.10.1")

    // Retrofit za mrežne pozive
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Za parsiranje JSON-a

    // Material Design Components za kartice i druge UI elemente
    implementation("com.google.android.material:material:1.12.0")

    // Za lakše upravljanje asinkronim zadacima (opcionalno, ali preporučljivo)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.cardview:cardview:1.0.0")


}
