package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.statekeeper.coding.DefaultDecoder
import com.arkivanov.essenty.statekeeper.coding.DefaultEncoder
import com.arkivanov.essenty.statekeeper.coding.StreamedDataInput
import com.arkivanov.essenty.statekeeper.coding.StreamedDataOutput
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

internal actual fun <T : Any> T.serialize(strategy: SerializationStrategy<T>): ByteArray =
    ByteArrayOutputStream().use { output ->
        ZipOutputStream(output).use { zip ->
            zip.setLevel(7)
            zip.putNextEntry(ZipEntry(""))

            zip.buffered().use { bufferedOutput ->
                DefaultEncoder(output = StreamedDataOutput(output = bufferedOutput))
                    .encodeSerializableValue(serializer = strategy, value = this)
            }
        }

        output.toByteArray()
    }

internal actual fun <T : Any> ByteArray.deserialize(strategy: DeserializationStrategy<T>): T =
    ZipInputStream(ByteArrayInputStream(this)).use { zip ->
        zip.nextEntry

        zip.buffered().use { bufferedInput ->
            DefaultDecoder(input = StreamedDataInput(input = bufferedInput))
                .decodeSerializableValue(deserializer = strategy)
        }
    }
