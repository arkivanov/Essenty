package com.arkivanov.essenty.instancekeeper

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
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
    fun GIVEN_instance_put_and_destroyed_WHEN_get_THEN_returns_instance() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance = TestInstance()
        dispatcher.put(key = "key", instance = instance)
        dispatcher.destroy()

        val returnedInstance = dispatcher.get(key = "key")

        assertSame(instance, returnedInstance)
    }

    @Test
    fun GIVEN_destroyed_WHEN_put_THEN_does_not_throw() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance = TestInstance()
        dispatcher.destroy()

        dispatcher.put(key = "key", instance = instance)
    }

    @Test
    fun GIVEN_destroyed_and_put_WHEN_get_THEN_returns_instance() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance = TestInstance()
        dispatcher.destroy()
        dispatcher.put(key = "key", instance = instance)

        val returnedInstance = dispatcher.get(key = "key")

        assertSame(instance, returnedInstance)
    }

    @Test
    fun GIVEN_not_empty_and_destroyed_WHEN_destroy_THEN_instance_is_not_destroyed_second_time() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance = TestInstance()
        dispatcher.put(key = "key", instance = instance)
        dispatcher.destroy()
        instance.isDestroyed = false

        dispatcher.destroy()

        assertFalse(instance.isDestroyed)
    }

    @Test
    fun GEVEN_instance_not_put_WHEN_remove_THEN_returns_null() {
        val dispatcher = DefaultInstanceKeeperDispatcher()

        val returnedInstance = dispatcher.remove(key = "key")

        assertNull(returnedInstance)
    }

    @Test
    fun GEVEN_instance_put_WHEN_remove_THEN_returns_instance() {
        val instance = TestInstance()
        val dispatcher = DefaultInstanceKeeperDispatcher()
        dispatcher.put("key", instance)

        val returnedInstance = dispatcher.remove(key = "key")

        assertSame(instance, returnedInstance)
    }

    @Test
    fun GEVEN_instance_removed_WHEN_remove_THEN_returns_null() {
        val instance = TestInstance()
        val dispatcher = DefaultInstanceKeeperDispatcher()
        dispatcher.put("key", instance)
        dispatcher.remove("key")

        val returnedInstance = dispatcher.remove(key = "key")

        assertNull(returnedInstance)
    }

    @Test
    fun GIVEN_instance_put_and_destroyed_WHEN_remove_THEN_returns_instance() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance = TestInstance()
        dispatcher.put(key = "key", instance = instance)
        dispatcher.destroy()

        val returnedInstance = dispatcher.remove(key = "key")

        assertSame(instance, returnedInstance)
    }

    @Test
    fun GIVEN_instance_put_and_destroyed_and_removed_WHEN_remove_THEN_returns_null() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        val instance = TestInstance()
        dispatcher.put(key = "key", instance = instance)
        dispatcher.destroy()
        dispatcher.remove(key = "key")

        val returnedInstance = dispatcher.remove(key = "key")

        assertNull(returnedInstance)
    }

    @Test
    fun GIVEN_instance_put_WHEN_destroy_and_instance_calls_get_in_onDestroy_THEN_returns_null() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        var returnedInstance: Any? = Any()
        dispatcher.put(key = "key", instance = TestInstance(onDestroy = { returnedInstance = null }))

        dispatcher.destroy()

        assertNull(returnedInstance)
    }

    @Test
    fun GIVEN_instances_put_WHEN_destroy_and_instance_calls_remove_in_onDestroy_THEN_does_not_throw() {
        val dispatcher = DefaultInstanceKeeperDispatcher()
        dispatcher.put(key = "key1", instance = TestInstance(onDestroy = { dispatcher.remove(key = "key1") }))
        dispatcher.put(key = "key2", instance = TestInstance(onDestroy = { dispatcher.remove(key = "key2") }))

        dispatcher.destroy()
    }

    private class TestInstance(
        private val onDestroy: () -> Unit = {},
    ) : InstanceKeeper.Instance {
        var isDestroyed: Boolean = false

        override fun onDestroy() {
            isDestroyed = true
            onDestroy.invoke()
        }
    }
}
