buildscript {

    extra.apply {
        set("compose_version", "1.3.0")
        set("roomVersion", "2.2.5")
        set("pagingVersion", "3.0.0-alpha07")
        set("lifecycle_version", "2.5.1")
        set("kotlin_version", "1.6.21")
    }
    repositories {
        // Make sure that you have the following two repositories
        google()  // Google's Maven repository
        mavenCentral()// Maven Central repository
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("com.google.gms:google-services:4.4.0")
        //classpath 'com.google.firebase:perf-plugin:1.4.1'
        //classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}