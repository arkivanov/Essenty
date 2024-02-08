package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

internal actual fun <T> T.serialize(strategy: SerializationStrategy<T>): ByteArray =
    ByteArrayOutputStream().use { output ->
        ZipOutputStream(output).use { zip ->
            zip.setLevel(7)
            zip.putNextEntry(ZipEntry("Entry"))

            zip.buffered().use { bufferedOutput ->
                @OptIn(ExperimentalSerializationApi::class)
                essentyJson.encodeToStream(serializer = strategy, value = this, stream = bufferedOutput)
            }
        }

        output.toByteArray()
    }

internal actual fun <T> ByteArray.deserialize(strategy: DeserializationStrategy<T>): T =
    ZipInputStream(ByteArrayInputStream(this)).use { zip ->
        zip.nextEntry

        zip.buffered().use { bufferedInput ->
            @OptIn(ExperimentalSerializationApi::class)
            essentyJson.decodeFromStream(deserializer = strategy, stream = bufferedInput)
        }
    }
