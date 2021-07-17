package com.arkivanov.essenty.backpressed

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DefaultBackPressedDispatcherTest {

    @Test
    fun GIVEN_handlers_subscribed_WHEN_onBackPressed_THEN_calls_handlers_in_reverse_order() {
        val events = ArrayList<String>()
        val dispatcher = DefaultBackPressedDispatcher()

        dispatcher.register {
            events += "handler1"
            false
        }

        dispatcher.register {
            events += "handler2"
            false
        }

        dispatcher.onBackPressed()

        assertEquals(events, listOf("handler2", "handler1"))
    }

    @Test
    fun GIVEN_handlers_subscribed_WHEN_onBackPressed_and_second_handler_returns_false_THEN_calls_first_handler() {
        var isCalled = false
        val dispatcher = DefaultBackPressedDispatcher()

        dispatcher.register {
            isCalled = true
            false
        }

        dispatcher.register { false }

        dispatcher.onBackPressed()

        assertTrue(isCalled)
    }

    @Test
    fun GIVEN_handlers_subscribed_WHEN_onBackPressed_and_second_handler_returns_true_THEN_does_not_call_first_handler() {
        var isCalled = false
        val dispatcher = DefaultBackPressedDispatcher()

        dispatcher.register {
            isCalled = true
            false
        }

        dispatcher.register { true }

        dispatcher.onBackPressed()

        assertFalse(isCalled)
    }

    @Test
    fun GIVEN_handler_unsubscribed_WHEN_onBackPressed_THEN_does_not_call_handler() {
        var isCalled = false

        val handler: () -> Boolean = {
            isCalled = true
            false
        }

        val dispatcher = DefaultBackPressedDispatcher()
        dispatcher.register(handler)
        dispatcher.unregister(handler)

        dispatcher.onBackPressed()

        assertFalse(isCalled)
    }

    @Test
    fun GIVEN_handlers_subscribed_WHEN_onBackPressed_and_all_handlers_return_false_THEN_onBackPressed_returns_false() {
        val dispatcher = DefaultBackPressedDispatcher()
        dispatcher.register { false }
        dispatcher.register { false }

        val result = dispatcher.onBackPressed()

        assertFalse(result)
    }

    @Test
    fun GIVEN_handlers_subscribed_WHEN_onBackPressed_and_first_handler_return_true_THEN_onBackPressed_returns_true() {
        val dispatcher = DefaultBackPressedDispatcher()
        dispatcher.register { true }
        dispatcher.register { false }

        val result = dispatcher.onBackPressed()

        assertTrue(result)
    }

    @Test
    fun GIVEN_handlers_subscribed_WHEN_onBackPressed_and_middle_handler_return_true_THEN_onBackPressed_returns_true() {
        val dispatcher = DefaultBackPressedDispatcher()
        dispatcher.register { false }
        dispatcher.register { true }
        dispatcher.register { false }

        val result = dispatcher.onBackPressed()

        assertTrue(result)
    }

    @Test
    fun GIVEN_handlers_subscribed_WHEN_onBackPressed_and_last_handler_return_true_THEN_onBackPressed_returns_true() {
        val dispatcher = DefaultBackPressedDispatcher()
        dispatcher.register { false }
        dispatcher.register { true }

        val result = dispatcher.onBackPressed()

        assertTrue(result)
    }
}
