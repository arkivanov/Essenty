package com.arkivanov.essenty.statekeeper

import kotlin.test.Test
import kotlin.test.assertEquals

class CodingTest {

    @Test
    fun serializes_and_deserializes() {
        val data = SerializableData()

        val newData = data.serialize(SerializableData.serializer()).deserialize(SerializableData.serializer())

        assertEquals(data, newData)
    }
}
