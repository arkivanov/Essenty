import com.arkivanov.gradle.bundle
import com.arkivanov.gradle.dependsOn
import com.arkivanov.gradle.setupBinaryCompatibilityValidator
import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupPublication
import com.arkivanov.gradle.setupSourceSets

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
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
        val android by bundle()
        val macosArm64 by bundle()

        java dependsOn common
        javaSet dependsOn java
        nonJava dependsOn common
        (allSet - javaSet) dependsOn nonJava

        common.main.dependencies {
            implementation(project(":utils-internal"))
            api(deps.jetbrains.kotlinx.kotlinxSerializationCore)
            implementation(deps.jetbrains.kotlinx.kotlinxSerializationJson)
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
