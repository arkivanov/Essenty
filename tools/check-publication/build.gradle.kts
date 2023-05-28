import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupSourceSets

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform()

repositories {
    maven("https://s01.oss.sonatype.org/content/groups/staging/") {
        credentials {
            username = "arkivanov"
            password = System.getenv("SONATYPE_PASSWORD")
        }
    }
}

android {
    namespace = "com.arkivanov.essenty.tools.checkpublication"
    compileSdkPreview = "UpsideDownCake"
}

kotlin {
    setupSourceSets {
        common.main.dependencies {
            val version = deps.versions.essenty.get()
            implementation("com.arkivanov.essenty:back-handler:$version")
            implementation("com.arkivanov.essenty:instance-keeper:$version")
            implementation("com.arkivanov.essenty:lifecycle:$version")
            implementation("com.arkivanov.essenty:parcelable:$version")
            implementation("com.arkivanov.essenty:state-keeper:$version")
        }
    }
}
