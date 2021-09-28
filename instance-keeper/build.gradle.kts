plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
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
                implementation(deps.androidx.lifecycle.lifecycleViewmodelKtx)
            }
        }
    }
}
