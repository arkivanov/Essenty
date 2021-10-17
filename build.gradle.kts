import com.arkivanov.gradle.AndroidConfig
import com.arkivanov.gradle.PublicationConfig
import com.arkivanov.gradle.Target
import com.arkivanov.gradle.named
import com.arkivanov.gradle.nativeSet
import io.gitlab.arturbosch.detekt.detekt
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
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.14.2")
    id("com.arkivanov.gradle.setup")
}

setupDefaults {
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
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }

    plugins.apply("io.gitlab.arturbosch.detekt")

    detekt {
        toolVersion = "1.14.2"
        parallel = true
        config = files("$rootDir/detekt.yml")
        input = files(file("src").listFiles()?.find { it.isDirectory } ?: emptyArray<Any>())
    }

    // Workaround until Detekt is updated: https://github.com/detekt/detekt/issues/3840.
    // The current version depends on kotlinx-html-jvm:0.7.2 which is not in Maven Central.
    configurations.named("detekt") {
        resolutionStrategy {
            force("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
        }
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
