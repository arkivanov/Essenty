import com.arkivanov.gradle.AndroidConfig
import com.arkivanov.gradle.PublicationConfig
import com.arkivanov.gradle.ensureUnreachableTasksDisabled
import com.arkivanov.gradle.iosCompat
import com.arkivanov.gradle.macosCompat
import com.arkivanov.gradle.setupDefaults
import com.arkivanov.gradle.setupDetekt
import com.arkivanov.gradle.tvosCompat
import com.arkivanov.gradle.watchosCompat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath(deps.kotlin.kotlinGradlePlug)
        classpath(deps.android.gradle)
        classpath(deps.kotlinx.binaryCompatibilityValidator)
        classpath(deps.detekt.gradleDetektPlug)
        classpath(deps.parcelizeDarwin.gradlePlug)
        classpath(deps.jetbrains.kotlin.serializationGradlePlug)
    }
}

plugins {
    id("com.arkivanov.gradle.setup")
}

setupDefaults(
    multiplatformConfigurator = {
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
    },
    androidConfig = AndroidConfig(
        minSdkVersion = 15,
        compileSdkVersion = 34,
        targetSdkVersion = 34,
    ),
    publicationConfig = PublicationConfig(
        group = "com.arkivanov.essenty",
        version = deps.versions.essenty.get(),
        projectName = "Essenty",
        projectDescription = "Essential libraries for Kotlin Multiplatform",
        projectUrl = "https://github.com/arkivanov/Essenty",
        scmUrl = "scm:git:git://github.com/arkivanov/Essenty.git",
        licenseName = "The Apache License, Version 2.0",
        licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt",
        developerId = "arkivanov",
        developerName = "Arkadii Ivanov",
        developerEmail = "arkann1985@gmail.com",
        signingKey = System.getenv("SIGNING_KEY"),
        signingPassword = System.getenv("SIGNING_PASSWORD"),
        repositoryUrl = "https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/${System.getenv("SONATYPE_REPOSITORY_ID")}",
        repositoryUserName = "arkivanov",
        repositoryPassword = System.getenv("SONATYPE_PASSWORD"),
    ),
)

setupDetekt()
ensureUnreachableTasksDisabled()

allprojects {
    repositories {
        mavenCentral()
        google()
    }

    afterEvaluate {
        extensions.findByType<KotlinMultiplatformExtension>()?.apply {
            sourceSets {
                all {
                    languageSettings.optIn("com.arkivanov.essenty.utils.internal.InternalEssentyApi")
                }
            }
        }
    }
}
