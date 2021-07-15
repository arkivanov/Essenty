package com.arkivanov.essenty.instancekeeper

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DefaultInstanceKeeperDispatcherTest {

    @Test
    fun GIVEN_no_instance_put_WHEN_get_THEN_returns_null() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        dispatcher.put(key = "key1", instance = TestInstance())

        val returnedInstance = dispatcher.get(key = "key2")

        assertNull(returnedInstance)
    }

    @Test
    fun GIVEN_instance_put_WHEN_get_THEN_returns_instance() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance1 = TestInstance()
        val instance2 = TestInstance()
        dispatcher.put(key = "key1", instance = instance1)
        dispatcher.put(key = "key2", instance = instance2)

        val returnedInstance1 = dispatcher.get(key = "key1")
        val returnedInstance2 = dispatcher.get(key = "key2")

        assertSame(instance1, returnedInstance1)
        assertSame(instance2, returnedInstance2)
    }

    @Test
    fun GIVEN_instance_put_WHEN_put_with_same_key_THEN_throws_exception() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        dispatcher.put(key = "key", instance = TestInstance())

        assertFails {
            dispatcher.put(key = "key", instance = TestInstance())
        }
    }

    @Test
    fun GIVEN_instance_with_same_key_put_twice_WHEN_get_THEN_returns_original_instance() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance = TestInstance()
        dispatcher.put(key = "key", instance = instance)
        try {
            dispatcher.put(key = "key", instance = TestInstance())
        } catch (ignored: Exception) {
        }

        val returnedInstance = dispatcher.get(key = "key")

        assertSame(instance, returnedInstance)
    }

    @Test
    fun GIVEN_instances_put_WHEN_destroy_THEN_instances_destroyed() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance1 = TestInstance()
        val instance2 = TestInstance()
        dispatcher.put(key = "key1", instance = instance1)
        dispatcher.put(key = "key2", instance = instance2)

        dispatcher.destroy()

        assertTrue(instance1.isDestroyed)
        assertTrue(instance2.isDestroyed)
    }

    @Test
    fun GIVEN_instance_destroyed_WHEN_get_THEN_returns_null() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        dispatcher.put(key = "key", instance = TestInstance())
        dispatcher.destroy()

        val returnedInstance = dispatcher.get(key = "key")

        assertNull(returnedInstance)
    }

    private class TestInstance : InstanceKeeper.Instance {
        var isDestroyed: Boolean = false

        override fun onDestroy() {
            isDestroyed = true
        }
    }
}
