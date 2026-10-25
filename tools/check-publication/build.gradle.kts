import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupSourceSets
import java.util.Base64

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform()

val sonatypeUserName =
    System.getenv("SONATYPE_USER_NAME")?.takeUnless(String::isBlank) ?: error("SONATYPE_USER_NAME is not set")

val sonatypePassword =
    System.getenv("SONATYPE_PASSWORD")?.takeUnless(String::isBlank) ?: error("SONATYPE_PASSWORD is not set")

val sonatypeToken: String =
    Base64.getEncoder().encodeToString("$sonatypeUserName:$sonatypePassword".toByteArray(Charsets.UTF_8))

repositories {
    maven("https://central.sonatype.com/api/v1/publisher/deployments/download/") {
        name = "deployments"
        credentials(HttpHeaderCredentials::class) {
            name = "Authorization"
            value = "Bearer $sonatypeToken"
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
