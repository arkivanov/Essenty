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
    fun WHEN_callback_set_THEN_enabled() {
        dispatcher.callbacks.put {}

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun WHEN_two_callbacks_set_THEN_enabled() {
        dispatcher.callbacks.put {}
        dispatcher.callbacks.put {}

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun WHEN_two_callbacks_set_one_disabled_THEN_enabled() {
        dispatcher.callbacks.put(callback = {}, isEnabled = false)
        dispatcher.callbacks.put {}

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_callbacks_set_WHEN_one_callback_disabled_THEN_enabled() {
        val callback1: () -> Unit = {}
        dispatcher.callbacks.put(callback1)
        dispatcher.callbacks.put {}

        dispatcher.callbacks.put(callback = callback1, isEnabled = false)

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_callbacks_set_WHEN_all_callbacks_disabled_THEN_disabled() {
        val callback1: () -> Unit = {}
        val callback2: () -> Unit = {}
        dispatcher.callbacks.put(callback1)
        dispatcher.callbacks.put(callback2)

        dispatcher.callbacks.put(callback = callback1, isEnabled = false)
        dispatcher.callbacks.put(callback = callback2, isEnabled = false)

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun WHEN_two_disabled_callbacks_set_THEN_disabled() {
        dispatcher.callbacks.put(callback = {}, isEnabled = false)
        dispatcher.callbacks.put(callback = {}, isEnabled = false)

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_callback_set_WHEN_callback_remove_THEN_disabled() {
        val callback1: () -> Unit = {}
        dispatcher.callbacks.put(callback1)

        dispatcher.callbacks.remove(callback1)

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_callbacks_set_WHEN_one_callback_removed_THEN_enabled() {
        val callback1: () -> Unit = {}
        dispatcher.callbacks.put(callback1)
        dispatcher.callbacks.put {}

        dispatcher.callbacks.remove(callback1)

        assertTrue(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_two_callbacks_set_WHEN_all_callback_removed_THEN_disabled() {
        val callback1: () -> Unit = {}
        val callback2: () -> Unit = {}
        dispatcher.callbacks.put(callback1)
        dispatcher.callbacks.put(callback2)

        dispatcher.callbacks.remove(callback1)
        dispatcher.callbacks.remove(callback2)

        assertFalse(dispatcher.isEnabled)
    }

    @Test
    fun GIVEN_callbacks_set_WHEN_back_THEN_last_callback_called() {
        val list = ArrayList<Int>()
        dispatcher.callbacks.put { list += 1 }
        dispatcher.callbacks.put { list += 2 }

        dispatcher.back()

        assertContentEquals(listOf(2), list)
    }

    @Test
    fun GIVEN_callbacks_not_set_WHEN_back_THEN_returned_false() {
        val result = dispatcher.back()

        assertFalse(result)
    }

    @Test
    fun GIVEN_callback_set_WHEN_back_THEN_returned_true() {
        dispatcher.callbacks.put {}

        val result = dispatcher.back()

        assertTrue(result)
    }

    @Test
    fun GIVEN_callbacks_set_and_then_some_disabled_WHEN_back_THEN_last_enabled_callback_called() {
        val list = ArrayList<Int>()
        val callback2: () -> Unit = { list += 2 }
        val callback4: () -> Unit = { list += 2 }
        dispatcher.callbacks.put { list += 1 }
        dispatcher.callbacks.put(callback2)
        dispatcher.callbacks.put { list += 3 }
        dispatcher.callbacks.put(callback4)
        dispatcher.callbacks[callback2] = false
        dispatcher.callbacks[callback4] = false

        dispatcher.back()

        assertContentEquals(listOf(3), list)
    }

    @Test
    fun GIVEN_callbacks_set_and_then_some_disabled_WHEN_back_THEN_returned_true() {
        val callback2: () -> Unit = {}
        val callback4: () -> Unit = {}
        dispatcher.callbacks.put {}
        dispatcher.callbacks.put(callback2)
        dispatcher.callbacks.put {}
        dispatcher.callbacks.put(callback4)
        dispatcher.callbacks[callback2] = false
        dispatcher.callbacks[callback4] = false

        val result = dispatcher.back()

        assertTrue(result)
    }

    @Test
    fun GIVEN_callbacks_set_and_some_disabled_WHEN_back_THEN_last_enabled_callback_called() {
        val list = ArrayList<Int>()
        dispatcher.callbacks.put { list += 1 }
        dispatcher.callbacks.put(callback = {}, isEnabled = false)
        dispatcher.callbacks.put { list += 3 }
        dispatcher.callbacks.put(callback = {}, isEnabled = false)

        dispatcher.back()

        assertContentEquals(listOf(3), list)
    }

    @Test
    fun GIVEN_callbacks_set_and_some_disabled_WHEN_back_THEN_returned_true() {
        dispatcher.callbacks.put {}
        dispatcher.callbacks.put(callback = {}, isEnabled = false)
        dispatcher.callbacks.put {}
        dispatcher.callbacks.put(callback = {}, isEnabled = false)

        val result = dispatcher.back()

        assertTrue(result)
    }

    @Test
    fun GIVEN_callbacks_set_and_then_all_disabled_WHEN_back_THEN_returned_false() {
        val callback1: () -> Unit = {}
        val callback2: () -> Unit = {}
        dispatcher.callbacks.put(callback1)
        dispatcher.callbacks.put(callback2)
        dispatcher.callbacks[callback1] = false
        dispatcher.callbacks[callback2] = false

        val result = dispatcher.back()

        assertFalse(result)
    }

    @Test
    fun GIVEN_callbacks_set_and_all_disabled_WHEN_back_THEN_returned_false() {
        dispatcher.callbacks.put(callback = {}, isEnabled = false)
        dispatcher.callbacks.put(callback = {}, isEnabled = false)

        val result = dispatcher.back()

        assertFalse(result)
    }
}
