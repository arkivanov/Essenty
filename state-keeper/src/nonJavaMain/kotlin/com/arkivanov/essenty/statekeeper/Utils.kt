package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

internal actual fun <T> T.serialize(strategy: SerializationStrategy<T>): ByteArray =
    essentyJson.encodeToString(serializer = strategy, value = this).encodeToByteArray()

internal actual fun <T> ByteArray.deserialize(strategy: DeserializationStrategy<T>): T =
    essentyJson.decodeFromString(deserializer = strategy, string = decodeToString())
