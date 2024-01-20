package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Suppress("TestFunctionName")
class SerializableContainerTest {

    @Test
    fun GIVEN_value_not_set_WHEN_consume_THEN_returns_null() {
        val container = SerializableContainer()

        val data = container.consume(SerializableData.serializer())

        assertNull(data)
    }

    @Test
    fun GIVEN_value_set_WHEN_consume_THEN_returns_value() {
        val data = SerializableData()
        val container = SerializableContainer(value = data, strategy = SerializableData.serializer())

        val newData = container.consume(SerializableData.serializer())

        assertEquals(data, newData)
    }

    @Test
    fun GIVEN_value_set_and_consumed_WHEN_consume_second_time_THEN_returns_null() {
        val container = SerializableContainer(value = SerializableData(), strategy = SerializableData.serializer())
        container.consume(SerializableData.serializer())

        val newData = container.consume(SerializableData.serializer())

        assertNull(newData)
    }

    @Test
    fun serializes_and_deserializes_data() {
        val data = SerializableData()
        val container = SerializableContainer(value = data, strategy = SerializableData.serializer())
        val newContainer = container.serializeAndDeserialize()
        val newData = newContainer.consume(strategy = SerializableData.serializer())

        assertEquals(data, newData)
    }

    @Test
    fun serializes_and_deserializes_data_twice() {
        val data = SerializableData()
        val container = SerializableContainer(value = data, strategy = SerializableData.serializer())
        val newContainer = container.serializeAndDeserialize().serializeAndDeserialize()
        val newData = newContainer.consume(strategy = SerializableData.serializer())

        assertEquals(data, newData)
    }

    @Test
    fun serializes_and_deserializes_null() {
        val container = SerializableContainer()
        val newContainer = container.serializeAndDeserialize()
        val newData = newContainer.consume(strategy = SerializableData.serializer())

        assertNull(newData)
    }

    @Test
    fun serializes_and_deserializes_null_twice() {
        val container = SerializableContainer()
        val newContainer = container.serializeAndDeserialize().serializeAndDeserialize()
        val newData = newContainer.consume(strategy = SerializableData.serializer())

        assertNull(newData)
    }

    @Test
    fun serializes_and_deserializes_list() {
        val data = SerializableData()

        val containers =
            Containers(
                listOf(
                    SerializableContainer(data, SerializableData.serializer()),
                    SerializableContainer(),
                    null,
                )
            )

        val newContainers = containers.serializeAndDeserialize(Containers.serializer())

        val (container1, container2, container3) = newContainers.list
        assertNotNull(container1)
        assertNotNull(container2)
        assertNull(container3)
        val newData1 = container1.consume(SerializableData.serializer())
        val newData2 = container2.consume(SerializableData.serializer())
        assertEquals(newData1, data)
        assertNull(newData2)
    }

    @Test
    fun serializes_and_deserializes_list_twice() {
        val data = SerializableData()

        val containers =
            Containers(
                listOf(
                    SerializableContainer(data, SerializableData.serializer()),
                    SerializableContainer(),
                    null,
                )
            )

        val newContainers = containers.serializeAndDeserialize(Containers.serializer()).serializeAndDeserialize(Containers.serializer())

        val (container1, container2, container3) = newContainers.list
        assertNotNull(container1)
        assertNotNull(container2)
        assertNull(container3)
        val newData1 = container1.consume(SerializableData.serializer())
        val newData2 = container2.consume(SerializableData.serializer())
        assertEquals(newData1, data)
        assertNull(newData2)
    }

    @Serializable
    private class Containers(
        val list: List<SerializableContainer?>,
    )
}
