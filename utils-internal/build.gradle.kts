import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupPublication

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

kotlin {
    linuxArm64()
}

setupMultiplatform()
setupPublication()

android {
    namespace = "com.arkivanov.essenty.utils.internal"
}
