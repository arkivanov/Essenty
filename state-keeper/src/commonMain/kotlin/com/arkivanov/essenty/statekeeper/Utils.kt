package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

private val defaultEssentyJson: Json =
    Json {
        allowStructuredMapKeys = true
    }

internal fun essentyJson(module: SerializersModule? = null): Json =
    if (module == null) {
        defaultEssentyJson
    } else {
        Json {
            allowStructuredMapKeys = true
            serializersModule = module
        }
    }

internal expect fun <T : Any> T.serialize(
    strategy: SerializationStrategy<T>,
    module: SerializersModule? = null,
): ByteArray

internal expect fun <T : Any> ByteArray.deserialize(
    strategy: DeserializationStrategy<T>,
    module: SerializersModule? = null,
): T
