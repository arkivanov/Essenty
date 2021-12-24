import com.arkivanov.gradle.AndroidConfig
import com.arkivanov.gradle.PublicationConfig
import com.arkivanov.gradle.Target
import com.arkivanov.gradle.named
import com.arkivanov.gradle.nativeSet
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        @Suppress("UnstableApiUsage")
        val deps = project.extensions.getByType<VersionCatalogsExtension>().named("deps") as org.gradle.accessors.dm.LibrariesForDeps

        classpath(deps.kotlin.kotlinGradlePlug)
        classpath(deps.android.gradle)
        classpath(deps.kotlinx.binaryCompatibilityValidatorGradlePlug)
        classpath(deps.detekt.gradleDetektPlug)
    }
}

plugins {
    id("com.arkivanov.gradle.setup")
}

setupAllProjects {
    multiplatformTargets(
        Target.Android,
        Target.Jvm,
        Target.Js(),
        Target.Linux,
        Target.Ios(),
        Target.WatchOs(),
        Target.TvOs(),
        Target.MacOs(),
    )

    multiplatformSourceSets {
        val nonNative by named(common)
        val nonAndroid by named(common)
        val native by named(nonAndroid)

        listOf(jvm, js).dependsOn(nonAndroid)
        listOf(android, jvm, js).dependsOn(nonNative)
        nativeSet.dependsOn(native)
    }

    androidConfig(
        AndroidConfig(
            minSdkVersion = 15,
            compileSdkVersion = 31,
            targetSdkVersion = 31,
        )
    )

    publicationConfig(
        PublicationConfig(
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
        )
    )

    detekt()
}

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
