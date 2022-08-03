package com.arkivanov.essenty.backhandler

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DefaultBackDispatcherTest {

    private val dispatcher = DefaultBackDispatcher()

    @Test
    fun WHEN_created_THEN_disabled() {
        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun WHEN_enabled_callback_registered_THEN_enabled() {
        dispatcher.register(callback(isEnabled = true))

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun WHEN_two_enabled_callbacks_registered_THEN_enabled() {
        dispatcher.register(callback(isEnabled = true))
        dispatcher.register(callback(isEnabled = true))

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun WHEN_two_callbacks_registered_one_disabled_THEN_enabled() {
        dispatcher.register(callback(isEnabled = false))
        dispatcher.register(callback(isEnabled = true))

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_enabled_callbacks_registered_WHEN_one_callback_disabled_THEN_enabled() {
        val callback1 = callback(isEnabled = true)
        dispatcher.register(callback1)
        dispatcher.register(callback(isEnabled = true))

        callback1.isEnabled = false

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_enabled_callbacks_registered_WHEN_all_callbacks_disabled_THEN_disabled() {
        val callback1 = callback(isEnabled = true)
        val callback2 = callback(isEnabled = true)
        dispatcher.register(callback1)
        dispatcher.register(callback2)

        callback1.isEnabled = false
        callback2.isEnabled = false

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun WHEN_two_disabled_callbacks_registered_THEN_disabled() {
        dispatcher.register(callback(isEnabled = false))
        dispatcher.register(callback(isEnabled = false))

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_enabled_callback_registered_WHEN_callback_unregistered_THEN_disabled() {
        val callback = callback(isEnabled = true)
        dispatcher.register(callback)

        dispatcher.unregister(callback)

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_enabled_callbacks_registered_WHEN_one_callback_unregistered_THEN_enabled() {
        val callback1 = callback(isEnabled = true)
        dispatcher.register(callback1)
        dispatcher.register(callback(isEnabled = true))

        dispatcher.unregister(callback1)

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_enabled_callbacks_registered_WHEN_all_callback_unregistered_THEN_disabled() {
        val callback1 = callback(isEnabled = true)
        val callback2 = callback(isEnabled = true)
        dispatcher.register(callback1)
        dispatcher.register(callback2)

        dispatcher.unregister(callback1)
        dispatcher.unregister(callback2)

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_enabled_callbacks_registered_WHEN_back_THEN_last_callback_called() {
        val list = ArrayList<Int>()
        dispatcher.register(callback(isEnabled = true) { list += 1 })
        dispatcher.register(callback(isEnabled = true) { list += 2 })

        dispatcher.back()

        assertContentEquals(listOf(2), list)
    }

    @Test
    fun GIVEN_callbacks_not_registered_WHEN_back_THEN_returned_false() {
        val result = dispatcher.back()

        assertFalse(result)
    }

    @Test
    fun GIVEN_enabled_callback_registered_WHEN_back_THEN_returned_true() {
        dispatcher.register(callback(isEnabled = true))

        val result = dispatcher.back()

        assertTrue(result)
    }

    @Test
    fun GIVEN_enabled_callbacks_registered_and_then_some_disabled_WHEN_back_THEN_last_enabled_callback_called() {
        val list = ArrayList<Int>()
        val callback2 = callback(isEnabled = true) { list += 2 }
        val callback4 = callback(isEnabled = true) { list += 4 }
        dispatcher.register(callback(isEnabled = true) { list += 1 })
        dispatcher.register(callback2)
        dispatcher.register(callback(isEnabled = true) { list += 3 })
        dispatcher.register(callback4)
        callback2.isEnabled = false
        callback4.isEnabled = false

        dispatcher.back()

        assertContentEquals(listOf(3), list)
    }

    @Test
    fun GIVEN_some_disabled_callbacks_registered_WHEN_back_THEN_returned_true() {
        val callback2 = callback(isEnabled = true)
        val callback4 = callback(isEnabled = true)
        dispatcher.register(callback(isEnabled = true))
        dispatcher.register(callback2)
        dispatcher.register(callback(isEnabled = true))
        dispatcher.register(callback4)
        callback2.isEnabled = false
        callback4.isEnabled = false

        val result = dispatcher.back()

        assertTrue(result)
    }

    @Test
    fun GIVEN_some_disabled_callbacks_registered_WHEN_back_THEN_last_enabled_callback_called() {
        val list = ArrayList<Int>()
        dispatcher.register(callback(isEnabled = true) { list += 1 })
        dispatcher.register(callback(isEnabled = false) { list += 2 })
        dispatcher.register(callback(isEnabled = true) { list += 3 })
        dispatcher.register(callback(isEnabled = false) { list += 4 })

        dispatcher.back()

        assertContentEquals(listOf(3), list)
    }

    @Test
    fun GIVEN_callbacks_registered_and_some_disabled_WHEN_back_THEN_returned_true() {
        dispatcher.register(callback(isEnabled = true))
        dispatcher.register(callback(isEnabled = false))
        dispatcher.register(callback(isEnabled = true))
        dispatcher.register(callback(isEnabled = false))

        val result = dispatcher.back()

        assertTrue(result)
    }

    @Test
    fun GIVEN_enabled_callbacks_registered_and_then_all_disabled_WHEN_back_THEN_returned_false() {
        val callback1 = callback(isEnabled = true)
        val callback2 = callback(isEnabled = true)
        dispatcher.register(callback1)
        dispatcher.register(callback2)
        callback1.isEnabled = false
        callback2.isEnabled = false

        val result = dispatcher.back()

        assertFalse(result)
    }

    @Test
    fun GIVEN_disabled_callbacks_registered_WHEN_back_THEN_returned_false() {
        dispatcher.register(callback = callback(isEnabled = false))
        dispatcher.register(callback = callback(isEnabled = false))

        val result = dispatcher.back()

        assertFalse(result)
    }

    private fun callback(isEnabled: Boolean = true, onBack: () -> Unit = {}): BackCallback =
        BackCallback(isEnabled = isEnabled, onBack = onBack)
}
