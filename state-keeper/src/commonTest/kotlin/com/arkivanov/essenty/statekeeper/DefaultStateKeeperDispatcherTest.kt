package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DefaultStateKeeperDispatcherTest {

    @Test
    fun WHEN_save_recreate_consume_THEN_data_restored() {
        val dispatcher1 = DefaultStateKeeperDispatcher(savedState = null)

        val data1 = Data()
        val data2 = Data()

        dispatcher1.register(key = "key1", strategy = Data.serializer()) { data1 }
        dispatcher1.register(key = "key2", strategy = Data.serializer()) { data2 }
        dispatcher1.register(key = "key3", strategy = Data.serializer()) { null }

        val savedState = dispatcher1.save()
            .serialize(strategy = SerializableContainer.serializer())
            .deserialize(strategy = SerializableContainer.serializer())

        val dispatcher2 = DefaultStateKeeperDispatcher(savedState = savedState)

        val restoredData1 = dispatcher2.consume(key = "key1", strategy = Data.serializer())
        val restoredData2 = dispatcher2.consume(key = "key2", strategy = Data.serializer())
        val restoredData3 = dispatcher2.consume(key = "key3", strategy = Data.serializer())

        assertEquals(data1, restoredData1)
        assertEquals(data2, restoredData2)
        assertNull(restoredData3)
    }

    @Test
    fun WHEN_consume_second_time_THEN_returns_null() {
        val dispatcher1 = DefaultStateKeeperDispatcher(savedState = null)

        dispatcher1.register(key = "key", strategy = Data.serializer()) { Data() }

        val savedState = dispatcher1.save().serializeAndDeserialize()

        val dispatcher2 = DefaultStateKeeperDispatcher(savedState = savedState)

        dispatcher2.consume(key = "key", strategy = Data.serializer())

        val restoredSerializable = dispatcher2.consume(key = "key", strategy = Data.serializer())

        assertNull(restoredSerializable)
    }

    @Test
    fun GIVEN_not_registered_WHEN_isRegistered_THEN_returns_false() {
        val dispatcher = DefaultStateKeeperDispatcher(savedState = null)

        val result = dispatcher.isRegistered(key = "key")

        assertFalse(result)
    }

    @Test
    fun GIVEN_registered_with_one_key_WHEN_isRegistered_with_another_key_THEN_returns_false() {
        val dispatcher = DefaultStateKeeperDispatcher(savedState = null)
        dispatcher.register(key = "key1", strategy = Data.serializer()) { Data() }

        val result = dispatcher.isRegistered(key = "key2")

        assertFalse(result)
    }

    @Test
    fun GIVEN_registered_WHEN_isRegistered_with_same_key_THEN_returns_true() {
        val dispatcher = DefaultStateKeeperDispatcher(savedState = null)
        dispatcher.register(key = "key", strategy = Data.serializer()) { Data() }

        val result = dispatcher.isRegistered(key = "key")

        assertTrue(result)
    }

    @Serializable
    private data class Data(
        val value: String,
    ) {
        constructor() : this(value = "value") // To avoid default values in the primary constructor
    }
}
