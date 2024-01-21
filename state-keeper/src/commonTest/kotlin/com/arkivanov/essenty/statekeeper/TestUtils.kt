package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.KSerializer

internal fun <T : Any> T.serializeAndDeserialize(serializer: KSerializer<T>): T =
    serialize(strategy = serializer)
        .deserialize(strategy = serializer)

internal fun SerializableContainer.serializeAndDeserialize(): SerializableContainer =
    serializeAndDeserialize(SerializableContainer.serializer())
