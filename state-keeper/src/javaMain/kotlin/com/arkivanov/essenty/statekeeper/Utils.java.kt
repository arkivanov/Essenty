package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.modules.SerializersModule
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

internal actual fun <T : Any> T.serialize(
    strategy: SerializationStrategy<T>,
    module: SerializersModule?,
): ByteArray =
    ByteArrayOutputStream().use { output ->
        ZipOutputStream(output).use { zip ->
            zip.setLevel(7)
            zip.putNextEntry(ZipEntry(""))

            zip.buffered().use { bufferedOutput ->
                @OptIn(ExperimentalSerializationApi::class)
                essentyJson(module = module).encodeToStream(serializer = strategy, value = this, stream = bufferedOutput)
            }
        }

        output.toByteArray()
    }

internal actual fun <T : Any> ByteArray.deserialize(
    strategy: DeserializationStrategy<T>,
    module: SerializersModule?,
): T =
    ZipInputStream(ByteArrayInputStream(this)).use { zip ->
        zip.nextEntry

        zip.buffered().use { bufferedInput ->
            @OptIn(ExperimentalSerializationApi::class)
            essentyJson(module = module).decodeFromStream(deserializer = strategy, stream = bufferedInput)
        }
    }
