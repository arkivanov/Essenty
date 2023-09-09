package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.statekeeper.coding.ByteArrayDataInput
import com.arkivanov.essenty.statekeeper.coding.ByteArrayDataOutput
import com.arkivanov.essenty.statekeeper.coding.DefaultDecoder
import com.arkivanov.essenty.statekeeper.coding.DefaultEncoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

internal actual fun <T : Any> T.serialize(strategy: SerializationStrategy<T>): ByteArray =
    ByteArrayDataOutput()
        .also { output ->
            DefaultEncoder(output = output)
                .encodeSerializableValue(serializer = strategy, value = this)
        }
        .getBytes()

internal actual fun <T : Any> ByteArray.deserialize(strategy: DeserializationStrategy<T>): T =
    DefaultDecoder(input = ByteArrayDataInput(bytes = this))
        .decodeSerializableValue(deserializer = strategy)
