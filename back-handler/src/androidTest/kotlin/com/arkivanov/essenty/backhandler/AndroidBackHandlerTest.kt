package com.arkivanov.essenty.backhandler

import androidx.activity.OnBackPressedDispatcher
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class AndroidBackHandlerTest {

    private val dispatcher = OnBackPressedDispatcher()
    private val handler = AndroidBackHandler(dispatcher)

    @Test
    fun WHEN_created_THEN_hasEnabledCallbacks_false() {
        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun WHEN_enabled_callback_put_THEN_hasEnabledCallbacks_true() {
        handler.callbacks.put(callback = {}, isEnabled = true)

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun WHEN_disabled_callback_put_THEN_hasEnabledCallbacks_false() {
        handler.callbacks.put(callback = {}, isEnabled = false)

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun WHEN_multiple_callbacks_put_and_one_enabled_THEN_hasEnabledCallbacks_true() {
        handler.callbacks.put(callback = {}, isEnabled = false)
        handler.callbacks.put(callback = {}, isEnabled = true)
        handler.callbacks.put(callback = {}, isEnabled = false)

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_disabled_callbacks_WHEN_one_callback_enabled_THEN_hasEnabledCallbacks_true() {
        val callback2: () -> Unit = {}
        handler.callbacks.put(callback = {}, isEnabled = false)
        handler.callbacks.put(callback = callback2, isEnabled = false)
        handler.callbacks.put(callback = {}, isEnabled = false)

        handler.callbacks.put(callback = callback2, isEnabled = true)

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_disabled_except_one_THEN_hasEnabledCallbacks_true() {
        val callbacks: List<() -> Unit> = listOf({}, {}, {})
        callbacks.forEach { callback ->
            handler.callbacks.put(callback = callback, isEnabled = true)
        }

        callbacks.drop(1).forEach { callback ->
            handler.callbacks.put(callback = callback, isEnabled = false)
        }

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_disabled_THEN_hasEnabledCallbacks_false() {
        val callbacks: List<() -> Unit> = listOf({}, {}, {})
        callbacks.forEach { callback ->
            handler.callbacks.put(callback = callback, isEnabled = true)
        }

        callbacks.forEach { callback ->
            handler.callbacks.put(callback = callback, isEnabled = false)
        }

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_removed_THEN_hasEnabledCallbacks_false() {
        val callbacks: List<() -> Unit> = listOf({}, {}, {})
        callbacks.forEach { callback ->
            handler.callbacks.put(callback = callback, isEnabled = true)
        }

        callbacks.forEach { callback ->
            handler.callbacks.remove(callback = callback)
        }

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_removed_except_one_THEN_hasEnabledCallbacks_true() {
        val callbacks: List<() -> Unit> = listOf({}, {}, {})
        callbacks.forEach { callback ->
            handler.callbacks.put(callback = callback, isEnabled = true)
        }

        callbacks.drop(1).forEach { callback ->
            handler.callbacks.remove(callback = callback)
        }

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_all_callbacks_disabled_WHEN_onBackPressed_THEN_callbacks_not_called() {
        var isCalled = false
        repeat(3) {
            handler.callbacks.put(callback = { isCalled = true }, isEnabled = false)
        }

        dispatcher.onBackPressed()

        assertFalse(isCalled)
    }

    @Test
    fun GIVEN_all_callbacks_enabled_WHEN_onBackPressed_THEN_only_last_callback_called() {
        val called = MutableList(3) { false }

        repeat(called.size) { index ->
            handler.callbacks.put(callback = { called[index] = true }, isEnabled = true)
        }

        dispatcher.onBackPressed()

        assertContentEquals(listOf(false, false, true), called)
    }

    @Test
    fun GIVEN_only_one_callback_enabled_WHEN_onBackPressed_THEN_only_enabled_callback_called() {
        val called = MutableList(3) { false }

        repeat(called.size) { index ->
            handler.callbacks.put(callback = { called[index] = true }, isEnabled = index == 0)
        }

        dispatcher.onBackPressed()

        assertContentEquals(listOf(true, false, false), called)
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_put_and_all_callbacks_removed_except_one_WHEN_onBackPressed_THEN_callback_called() {
        val called = MutableList(3) { false }
        val callbacks: List<() -> Unit> = List(called.size) { index -> { called[index] = true } }
        callbacks.forEach { callback ->
            handler.callbacks.put(callback = callback, isEnabled = true)
        }
        callbacks.drop(1).forEach { callback ->
            handler.callbacks.remove(callback = callback)
        }

        dispatcher.onBackPressed()

        assertContentEquals(listOf(true, false, false), called)
    }

    @Test
    fun WHEN_enabled_callback_put_THEN_get_returns_true() {
        val callback: () -> Unit = {}

        handler.callbacks.put(callback = callback, isEnabled = true)

        assertEquals(true, handler.callbacks.get(callback = callback))
    }

    @Test
    fun WHEN_disabled_callback_put_THEN_get_returns_false() {
        val callback: () -> Unit = {}

        handler.callbacks.put(callback = callback, isEnabled = false)

        assertEquals(false, handler.callbacks.get(callback = callback))
    }

    @Test
    fun GIVEN_enabled_callback_put_WHEN_callback_disabled_THEN_get_returns_false() {
        val callback: () -> Unit = {}
        handler.callbacks.put(callback = callback, isEnabled = true)

        handler.callbacks.put(callback = callback, isEnabled = false)

        assertEquals(false, handler.callbacks.get(callback = callback))
    }

    @Test
    fun WHEN_created_THEN_get_returns_null() {
        assertNull(handler.callbacks.get(callback = {}))
    }

    @Test
    fun GIVEN_callback_put_WHEN_callback_removed_THEN_get_returns_null() {
        val callback: () -> Unit = {}
        handler.callbacks.put(callback = callback, isEnabled = true)

        handler.callbacks.remove(callback = callback)

        assertNull(handler.callbacks.get(callback))
    }
}
