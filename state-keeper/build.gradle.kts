import com.arkivanov.gradle.bundle
import com.arkivanov.gradle.dependsOn
import com.arkivanov.gradle.setupBinaryCompatibilityValidator
import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupPublication
import com.arkivanov.gradle.setupSourceSets

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("com.arkivanov.parcelize.darwin")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform()
setupPublication()
setupBinaryCompatibilityValidator()

android {
    namespace = "com.arkivanov.essenty.statekeeper"
}

kotlin {
    setupSourceSets {
        val java by bundle()
        val nonJava by bundle()
        val js by bundle()
        val nonJs by bundle()
        val android by bundle()

        java dependsOn common
        javaSet dependsOn java
        nonJava dependsOn common
        (allSet - javaSet) dependsOn nonJava
        nonJs dependsOn common
        (allSet - js) dependsOn nonJs

        common.main.dependencies {
            api(project(":parcelable"))
            implementation(project(":utils-internal"))
            api(deps.jetbrains.kotlinx.kotlinxSerializationCore)
        }

        android.main.dependencies {
            implementation(deps.androidx.savedstate.savedstateKtx)
            implementation(deps.androidx.lifecycle.lifecycleRuntime)
        }

        android.test.dependencies {
            implementation(deps.robolectric.robolectric)
        }
    }
}
