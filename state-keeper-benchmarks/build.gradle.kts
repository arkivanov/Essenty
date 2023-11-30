import com.arkivanov.gradle.setupAndroidLibrary

plugins {
    id("kotlin-android")
    id("com.android.library")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    id("com.arkivanov.gradle.setup")
}

setupAndroidLibrary()

android {
    namespace = "com.arkivanov.essenty.statekeeper.benchmarks"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(project(":state-keeper"))
    implementation(deps.jetbrains.kotlinx.kotlinxSerializationJson)
    testImplementation(kotlin("test"))
    testImplementation(deps.robolectric.robolectric)
}
