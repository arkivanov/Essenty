package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

internal val essentyJson: Json =
    Json {
        allowStructuredMapKeys = true
    }

internal expect fun <T : Any> T.serialize(strategy: SerializationStrategy<T>): ByteArray

internal expect fun <T : Any> ByteArray.deserialize(strategy: DeserializationStrategy<T>): T
