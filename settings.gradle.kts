enableFeaturePreview("VERSION_CATALOGS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            from(files("deps.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://jitpack.io")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.toString() == "com.arkivanov.gradle.setup") {
                useModule("com.github.arkivanov:gradle-setup-plugin:29d636a815")
            }
        }
    }

    plugins {
        id("com.arkivanov.gradle.setup")
    }
}

include(":utils-internal")
include(":lifecycle")
include(":parcelable")
include(":state-keeper")
include(":instance-keeper")
include(":back-pressed")

if (startParameter.projectProperties.containsKey("check_publication")) {
    include(":tools:check-publication")
}
