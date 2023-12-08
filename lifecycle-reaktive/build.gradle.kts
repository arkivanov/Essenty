import com.arkivanov.gradle.iosCompat
import com.arkivanov.gradle.macosCompat
import com.arkivanov.gradle.setupBinaryCompatibilityValidator
import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupPublication
import com.arkivanov.gradle.setupSourceSets
import com.arkivanov.gradle.tvosCompat
import com.arkivanov.gradle.watchosCompat

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

// Reaktive doesn't support wasm yet
setupMultiplatform {
    androidTarget()
    jvm()
    js {
        browser()
        nodejs()
    }
    linuxX64()
    iosCompat()
    watchosCompat()
    tvosCompat()
    macosCompat()
}

setupPublication()
setupBinaryCompatibilityValidator()

android {
    namespace = "com.arkivanov.essenty.lifecycle.reaktive"
}

kotlin {
    setupSourceSets {
        common.main.dependencies {
            implementation(project(":lifecycle"))
            implementation(deps.reaktive.reaktive)
        }
    }
}
