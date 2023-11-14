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
    namespace = "com.arkivanov.essenty.lifecycle.coroutines"
}

kotlin {
    setupSourceSets {
        val android by bundle()

        common.main.dependencies {
            implementation(project(":utils-internal"))
            implementation(project(":lifecycle"))
            implementation(deps.kotlinx.coroutinesCore)
        }

        common.test.dependencies {
            implementation(deps.kotlinx.coroutinesTest)
        }

        android.main.dependencies {
            implementation(deps.androidx.lifecycle.lifecycleCommonJava8)
            implementation(deps.androidx.lifecycle.lifecycleRuntime)
        }
    }
}
