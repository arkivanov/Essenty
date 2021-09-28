plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.arkivanov.gradle.setup")
}

setup {
    multiplatform()
    multiplatformPublications()
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":utils-internal"))
            }
        }

        named("androidMain") {
            dependencies {
                implementation(deps.androidx.core.coreKtx)
            }
        }

        named("androidTest") {
            dependencies {
                implementation(deps.robolectric.robolectric)
            }
        }
    }
}
