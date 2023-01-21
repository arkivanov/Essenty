import com.arkivanov.gradle.bundle
import com.arkivanov.gradle.setupBinaryCompatibilityValidator
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
setupBinaryCompatibilityValidator()

android {
    namespace = "com.arkivanov.essenty.lifecycle"
}

kotlin {
    setupSourceSets {
        val android by bundle()

        common.main.dependencies {
            implementation(project(":utils-internal"))
        }

        android.main.dependencies {
            implementation(deps.androidx.lifecycle.lifecycleCommonJava8)
            implementation(deps.androidx.lifecycle.lifecycleRuntime)
        }
    }
}
