plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.elahee.testapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.elahee.testapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 52
        versionName = "2.2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable= false
            isMinifyEnabled = true
            isShrinkResources= true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable= true
            isMinifyEnabled= true
            isShrinkResources= true
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    lint {

        checkReleaseBuilds= false

    }
    bundle {
        language {
            enableSplit = false
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.runtime:runtime-rxjava2")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("androidx.activity:activity:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    val kotlin_version = rootProject.extra["kotlin_version"]
    val lifecycle_version = rootProject.extra["lifecycle_version"]
    val pagingVersion = rootProject.extra["pagingVersion"]
    val roomVersion = rootProject.extra["roomVersion"]
    val compose_version = rootProject.extra["compose_version"]

    implementation ("androidx.core:core-splashscreen:1.0.1")
    //Kotlin Support
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation ("androidx.core:core-ktx:1.8.0")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.paging:paging-runtime:3.1.1")

    //RxJava
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.12")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.6.0")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    // Retrofit and OkHttp libraries for network call;
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.5.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.1.0")
    implementation ("com.google.maps.android:android-maps-utils:2.3.0")

    implementation ("com.github.bumptech.glide:glide:4.14.2")
    implementation ("io.github.chaosleung:pinview:1.4.4")
    implementation ("com.karumi:dexter:6.2.3")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //SmsRetriver
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.google.firebase:firebase-crashlytics")
    implementation ("com.google.firebase:firebase-messaging-ktx")
    implementation ("com.google.firebase:firebase-config-ktx")

    //dagger-hilt
    implementation ("com.google.dagger:hilt-android:2.48.1")
    kapt ("com.google.dagger:hilt-android-compiler:2.48.1")
    // When using Kotlin.
    kapt ("androidx.hilt:hilt-compiler:1.0.0")

    implementation ("androidx.work:work-runtime-ktx:2.7.1")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    //inappupdates
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

}
kapt {
    correctErrorTypes= true
}