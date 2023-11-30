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
                useModule("com.github.arkivanov:gradle-setup-plugin:4a116614b3")
            }
        }
    }

    plugins {
        id("com.arkivanov.gradle.setup")
    }
}

if (!startParameter.projectProperties.containsKey("check_publication")) {
    include(":utils-internal")
    include(":lifecycle")
    include(":lifecycle-coroutines")
    include(":lifecycle-reaktive")
    include(":state-keeper")
    include(":state-keeper-benchmarks")
    include(":instance-keeper")
    include(":back-handler")
} else {
    include(":tools:check-publication")
}
