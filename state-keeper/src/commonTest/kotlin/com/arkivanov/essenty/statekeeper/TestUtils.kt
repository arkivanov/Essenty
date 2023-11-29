package com.arkivanov.essenty.statekeeper

internal fun SerializableContainer.serializeAndDeserialize(): SerializableContainer =
    serialize(strategy = SerializableContainer.serializer())
        .deserialize(strategy = SerializableContainer.serializer())
