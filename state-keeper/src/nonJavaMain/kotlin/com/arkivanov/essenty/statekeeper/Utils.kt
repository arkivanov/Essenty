package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

internal actual fun <T : Any> T.serialize(strategy: SerializationStrategy<T>): ByteArray =
    essentyJson.encodeToString(serializer = strategy, value = this).encodeToByteArray()

internal actual fun <T : Any> ByteArray.deserialize(strategy: DeserializationStrategy<T>): T =
    essentyJson.decodeFromString(deserializer = strategy, string = decodeToString())
