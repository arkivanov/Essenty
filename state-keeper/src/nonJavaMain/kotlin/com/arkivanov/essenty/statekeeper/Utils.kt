package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule

internal actual fun <T : Any> T.serialize(
    strategy: SerializationStrategy<T>,
    module: SerializersModule?,
): ByteArray =
    essentyJson(module).encodeToString(serializer = strategy, value = this).encodeToByteArray()

internal actual fun <T : Any> ByteArray.deserialize(
    strategy: DeserializationStrategy<T>,
    module: SerializersModule?,
): T =
    essentyJson(module).decodeFromString(deserializer = strategy, string = decodeToString())
