import com.arkivanov.gradle.bundle
import com.arkivanov.gradle.dependsOn
import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupPublication
import com.arkivanov.gradle.setupSourceSets

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform()
setupPublication()

kotlin {
    setupSourceSets {
        val js by bundle()
        val native by bundle()
        val nonNative by bundle()

        native dependsOn common
        nonNative dependsOn common
        nativeSet dependsOn native
        (allSet - nativeSet) dependsOn nonNative
    }
}
