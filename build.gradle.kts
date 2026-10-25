import com.arkivanov.gradle.AndroidConfig
import com.arkivanov.gradle.BinaryCompatibilityValidatorConfig
import com.arkivanov.gradle.PublicationConfig
import com.arkivanov.gradle.ensureUnreachableTasksDisabled
import com.arkivanov.gradle.iosCompat
import com.arkivanov.gradle.macosCompat
import com.arkivanov.gradle.setupDefaults
import com.arkivanov.gradle.setupDetekt
import com.arkivanov.gradle.tvosCompat
import com.arkivanov.gradle.watchosCompat
import groovy.json.JsonSlurper
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.util.Base64

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
        classpath(deps.jetbrains.kotlin.serializationGradlePlug)
    }
}

plugins {
    id("com.arkivanov.gradle.setup")
}

val publicationGroup = "com.arkivanov.essenty"
val publicationVersion = deps.versions.essenty.get()

setupDefaults(
    multiplatformConfigurator = {
        androidTarget()
        jvm()
        js {
            browser()
            nodejs()
        }
        wasmJs {
            browser()
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
        group = publicationGroup,
        version = publicationVersion,
        projectName = "Essenty",
        projectDescription = "Essential libraries for Kotlin Multiplatform",
        projectUrl = "https://github.com/arkivanov/Essenty",
        scmUrl = "scm:git:git://github.com/arkivanov/Essenty.git",
        licenseName = "Apache-2.0",
        licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt",
        developerId = "arkivanov",
        developerName = "Arkadii Ivanov",
        developerEmail = "arkann1985@gmail.com",
        signingKey = System.getenv("SIGNING_KEY"),
        signingPassword = System.getenv("SIGNING_PASSWORD"),
        repositoryUrl = "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/",
        repositoryUserName = System.getenv("SONATYPE_USER_NAME"),
        repositoryPassword = System.getenv("SONATYPE_PASSWORD"),
    ),
    binaryCompatibilityValidatorConfig = BinaryCompatibilityValidatorConfig(klib = false),
)

setupCentralPortalUpload(deploymentName = "Essenty $publicationVersion")

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

// Uploads all publications as a single deployment bundle via the Central Portal Publisher API:
// https://central.sonatype.org/publish/publish-portal-api/
//
// Usage: ./gradlew publishAllPublicationsToCentralPortalBundleRepository && ./gradlew uploadCentralPortalBundle
fun Project.setupCentralPortalUpload(deploymentName: String) {
    val bundleDir = layout.buildDirectory.dir("central-portal/repository")

    // A separate repository, because Gradle rejects credentials for file repositories,
    // and the plugin's repository always has credentials configured.
    subprojects {
        plugins.withId("maven-publish") {
            extensions.configure<PublishingExtension> {
                repositories.maven {
                    name = "centralPortalBundle"
                    url = bundleDir.get().asFile.toURI()
                }
            }
        }
    }

    val zipTask =
        tasks.register<Zip>("zipCentralPortalBundle") {
            group = "publishing"
            description = "Zips the local publication repository into a Central Portal deployment bundle."
            from(bundleDir)
            // The Portal does not accept maven-metadata files in bundles
            exclude("**/maven-metadata*")
            destinationDirectory.set(layout.buildDirectory.dir("central-portal"))
            archiveFileName.set("bundle.zip")
        }

    tasks.register<UploadCentralPortalBundleTask>("uploadCentralPortalBundle") {
        group = "publishing"
        description = "Uploads the bundle to the Central Portal and waits for validation. Run after the publish task."
        bundle.set(zipTask.flatMap { it.archiveFile })
        this.deploymentName.set(deploymentName)
        userName.set(providers.environmentVariable("SONATYPE_USER_NAME"))
        password.set(providers.environmentVariable("SONATYPE_PASSWORD"))
    }
}

abstract class UploadCentralPortalBundleTask : DefaultTask() {

    @get:InputFile
    abstract val bundle: RegularFileProperty

    @get:Input
    abstract val deploymentName: Property<String>

    @get:Internal
    abstract val userName: Property<String>

    @get:Internal
    abstract val password: Property<String>

    @TaskAction
    fun upload() {
        val token = Base64.getEncoder().encodeToString("${userName.get()}:${password.get()}".toByteArray())
        val deploymentId = uploadBundle(token = token)
        logger.lifecycle("Uploaded deployment $deploymentId")
        awaitValidation(token = token, deploymentId = deploymentId)
    }

    private fun uploadBundle(token: String): String {
        val file = bundle.get().asFile
        val boundary = "----${System.nanoTime()}"
        val head =
            "--$boundary\r\nContent-Disposition: form-data; name=\"bundle\"; filename=\"${file.name}\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n"
        val headBytes = head.toByteArray()
        val tailBytes = "\r\n--$boundary--\r\n".toByteArray()
        val name = URLEncoder.encode(deploymentName.get(), Charsets.UTF_8)

        return request(
            method = "POST",
            path = "/upload?publishingType=USER_MANAGED&name=$name",
            token = token,
        ) {
            setFixedLengthStreamingMode(headBytes.size + file.length() + tailBytes.size)
            setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            doOutput = true
            outputStream.use { output ->
                output.write(headBytes)
                file.inputStream().use { it.copyTo(output) }
                output.write(tailBytes)
            }
        }.trim()
    }

    private fun awaitValidation(token: String, deploymentId: String) {
        val deadline = System.currentTimeMillis() + 30 * 60 * 1000L
        var lastState: String? = null

        while (true) {
            val status = JsonSlurper().parseText(request(method = "POST", path = "/status?id=$deploymentId", token = token)) as Map<*, *>
            val state = status["deploymentState"] as String

            if (state != lastState) {
                logger.lifecycle("Deployment $deploymentId: $state")
                lastState = state
            }

            when (state) {
                "PENDING", "VALIDATING" -> {
                    check(System.currentTimeMillis() < deadline) { "Timed out waiting for deployment $deploymentId to be validated" }
                    Thread.sleep(10_000L)
                }

                "FAILED" -> throw GradleException("Deployment $deploymentId failed: ${status["errors"]}")
                else -> return
            }
        }
    }

    private fun request(method: String, path: String, token: String, configure: HttpURLConnection.() -> Unit = {}): String {
        val connection = URI("https://central.sonatype.com/api/v1/publisher$path").toURL().openConnection() as HttpURLConnection
        try {
            connection.requestMethod = method
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.configure()
            val code = connection.responseCode
            val body = (if (code in 200..299) connection.inputStream else connection.errorStream)?.use { it.reader().readText() }.orEmpty()
            check(code in 200..299) { "$method $path failed with HTTP $code: $body" }
            return body
        } finally {
            connection.disconnect()
        }
    }
}
