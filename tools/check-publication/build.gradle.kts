import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupSourceSets

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform()

repositories {
    maven("https://central.sonatype.com/api/v1/publisher/deployments/download/") {
        name = "deployments"
        credentials(HttpHeaderCredentials::class) {
            name = "Authorization"
            value = "Bearer ${System.getenv("SONATYPE_AUTH_BASE64")}"
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
}

android {
    namespace = "com.arkivanov.essenty.tools.checkpublication"
}

kotlin {
    setupSourceSets {
        common.main.dependencies {
            val version = deps.versions.essenty.get()
            implementation("com.arkivanov.essenty:back-handler:$version")
            implementation("com.arkivanov.essenty:instance-keeper:$version")
            implementation("com.arkivanov.essenty:lifecycle:$version")
            implementation("com.arkivanov.essenty:lifecycle-coroutines:$version")
            implementation("com.arkivanov.essenty:lifecycle-reaktive:$version")
            implementation("com.arkivanov.essenty:state-keeper:$version")
        }
    }
}
