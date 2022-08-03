package com.arkivanov.essenty.backhandler

import androidx.activity.OnBackPressedDispatcher
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
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
    fun WHEN_enabled_callback_registered_THEN_hasEnabledCallbacks_true() {
        handler.register(callback(isEnabled = true))

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun WHEN_disabled_callback_registered_THEN_hasEnabledCallbacks_false() {
        handler.register(callback(isEnabled = false))

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun WHEN_multiple_callbacks_registered_and_one_enabled_THEN_hasEnabledCallbacks_true() {
        handler.register(callback(isEnabled = false))
        handler.register(callback(isEnabled = true))
        handler.register(callback(isEnabled = false))

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_disabled_callbacks_WHEN_one_callback_enabled_THEN_hasEnabledCallbacks_true() {
        val callback2 = callback(isEnabled = false)
        handler.register(callback(isEnabled = false))
        handler.register(callback2)
        handler.register(callback(isEnabled = false))

        callback2.isEnabled = true

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_disabled_except_one_THEN_hasEnabledCallbacks_true() {
        val callbacks = listOf(callback(isEnabled = true), callback(isEnabled = true), callback(isEnabled = true))
        callbacks.forEach(handler::register)

        callbacks.drop(1).forEach { it.isEnabled = false }

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_disabled_THEN_hasEnabledCallbacks_false() {
        val callbacks = listOf(callback(isEnabled = true), callback(isEnabled = true), callback(isEnabled = true))
        callbacks.forEach(handler::register)

        callbacks.forEach { it.isEnabled = false }

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_removed_THEN_hasEnabledCallbacks_false() {
        val callbacks = listOf(callback(isEnabled = true), callback(isEnabled = true), callback(isEnabled = true))
        callbacks.forEach(handler::register)

        callbacks.forEach(handler::unregister)

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_WHEN_all_callbacks_removed_except_one_THEN_hasEnabledCallbacks_true() {
        val callbacks = listOf(callback(isEnabled = true), callback(isEnabled = true), callback(isEnabled = true))
        callbacks.forEach(handler::register)

        callbacks.drop(1).forEach(handler::unregister)

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_all_callbacks_disabled_WHEN_onBackPressed_THEN_callbacks_not_called() {
        var isCalled = false
        repeat(3) {
            handler.register(callback(isEnabled = false) { isCalled = true })
        }

        dispatcher.onBackPressed()

        assertFalse(isCalled)
    }

    @Test
    fun GIVEN_all_callbacks_enabled_WHEN_onBackPressed_THEN_only_last_callback_called() {
        val called = MutableList(3) { false }

        repeat(called.size) { index ->
            handler.register(callback(isEnabled = true) { called[index] = true })
        }

        dispatcher.onBackPressed()

        assertContentEquals(listOf(false, false, true), called)
    }

    @Test
    fun GIVEN_only_one_callback_enabled_WHEN_onBackPressed_THEN_only_enabled_callback_called() {
        val called = MutableList(3) { false }

        repeat(called.size) { index ->
            handler.register(callback(isEnabled = index == 0) { called[index] = true })
        }

        dispatcher.onBackPressed()

        assertContentEquals(listOf(true, false, false), called)
    }

    @Test
    fun GIVEN_multiple_enabled_callbacks_registered_and_all_callbacks_removed_except_one_WHEN_onBackPressed_THEN_callback_called() {
        val called = MutableList(3) { false }
        val callbacks = List(called.size) { index -> callback(isEnabled = true) { called[index] = true } }
        callbacks.forEach(handler::register)
        callbacks.drop(1).forEach(handler::unregister)

        dispatcher.onBackPressed()

        assertContentEquals(listOf(true, false, false), called)
    }

    private fun callback(isEnabled: Boolean = true, onBack: () -> Unit = {}): BackCallback =
        BackCallback(isEnabled = isEnabled, onBack = onBack)
}
