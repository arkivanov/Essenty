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
    namespace = "com.arkivanov.essenty.parcelable"
}

kotlin {
    setupSourceSets {
        val android by bundle()
        val jvm by bundle()
        val darwin by bundle()
        val notParcelable by bundle()

        notParcelable dependsOn common
        (allSet - android - jvm - darwinSet) dependsOn notParcelable
        darwin dependsOn common
        darwinSet dependsOn darwin

        common.main.dependencies {
            implementation(project(":utils-internal"))
        }

        android.main.dependencies {
            implementation(deps.androidx.core.coreKtx)
        }

        android.test.dependencies {
            implementation(deps.robolectric.robolectric)
        }

        darwin.main.dependencies {
            implementation(deps.parcelizeDarwin.runtime)
        }
    }
}
