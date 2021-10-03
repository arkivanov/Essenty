plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

setup {
    multiplatform()
}

repositories {
    maven("https://s01.oss.sonatype.org/content/groups/staging/") {
        credentials {
            username = "arkivanov"
            password = System.getenv("SONATYPE_PASSWORD")
        }
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                val version = deps.versions.essenty.get()
                implementation("com.arkivanov.essenty:lifecycle:$version")
                implementation("com.arkivanov.essenty:state-keeper:$version")
                implementation("com.arkivanov.essenty:instance-keeper:$version")
                implementation("com.arkivanov.essenty:back-pressed:$version")
            }
        }
    }
}
