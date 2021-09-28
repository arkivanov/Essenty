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
                api(project(":parcelable"))
                implementation(project(":utils-internal"))
            }
        }

        named("androidMain") {
            dependencies {
                implementation(deps.androidx.savedstate.savedstateKtx)
                implementation(deps.androidx.lifecycle.lifecycleRuntime)
            }
        }
    }
}
