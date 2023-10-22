package com.arkivanov.essenty.backhandler

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
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
    fun GIVEN_enabled_callbacks_registered_with_priorities_WHEN_back_THEN_last_callback_with_higher_priority_called() {
        val list = ArrayList<Int>()
        dispatcher.register(callback(isEnabled = true, priority = 0) { list += 1 })
        dispatcher.register(callback(isEnabled = true, priority = 1) { list += 2 })
        dispatcher.register(callback(isEnabled = true, priority = 2) { list += 3 })
        dispatcher.register(callback(isEnabled = true, priority = 1) { list += 4 })
        dispatcher.register(callback(isEnabled = true, priority = 2) { list += 5 })
        dispatcher.register(callback(isEnabled = true, priority = 0) { list += 6 })
        dispatcher.register(callback(isEnabled = true, priority = 1) { list += 7 })
        dispatcher.register(callback(isEnabled = true, priority = 0) { list += 8 })

        dispatcher.back()

        assertContentEquals(listOf(5), list)
    }

    @Test
    fun GIVEN_enabled_callbacks_registered_with_priorities_WHEN_priority_changed_and_back_THEN_last_callback_with_higher_priority_called() {
        val list = ArrayList<Int>()
        dispatcher.register(callback(isEnabled = true, priority = 0) { list += 1 })
        val callback = callback(isEnabled = true, priority = 1) { list += 2 }
        dispatcher.register(callback)
        dispatcher.register(callback(isEnabled = true, priority = 2) { list += 3 })

        callback.priority = 3
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

    @Test
    fun GIVEN_callback_not_registered_WHEN_startPredictiveBack_THEN_returns_null() {
        val predictiveBackDispatcher = dispatcher.startPredictiveBack(BackEvent())

        assertNull(predictiveBackDispatcher)
    }

    @Test
    fun WHEN_progress_with_finish_THEN_callbacks_called() {
        val startEvent = BackEvent(progress = 0.1F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 1F, touchY = 2F)
        val progressEvent1 = BackEvent(progress = 0.2F, swipeEdge = BackEvent.SwipeEdge.RIGHT, touchX = 2F, touchY = 3F)
        val progressEvent2 = BackEvent(progress = 0.3F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 3F, touchY = 4F)
        val receivedEvents = ArrayList<Any?>()

        dispatcher.register(
            BackCallback(
                onBackStarted = { receivedEvents += it },
                onBackProgressed = { receivedEvents += it },
                onBackCancelled = { receivedEvents += "Cancel" },
                onBack = { receivedEvents += "Back" },
            )
        )

        val predictiveBackDispatcher = assertNotNull(dispatcher.startPredictiveBack(startEvent))
        predictiveBackDispatcher.progress(progressEvent1)
        predictiveBackDispatcher.progress(progressEvent2)
        predictiveBackDispatcher.finish()

        assertContentEquals(listOf(startEvent, progressEvent1, progressEvent2, "Back"), receivedEvents)
    }

    @Test
    fun WHEN_progress_with_cancel_THEN_callbacks_called() {
        val startEvent = BackEvent(progress = 0.1F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 1F, touchY = 2F)
        val progressEvent1 = BackEvent(progress = 0.2F, swipeEdge = BackEvent.SwipeEdge.RIGHT, touchX = 2F, touchY = 3F)
        val progressEvent2 = BackEvent(progress = 0.3F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 3F, touchY = 4F)
        val receivedEvents = ArrayList<Any?>()

        dispatcher.register(
            BackCallback(
                onBackStarted = { receivedEvents += it },
                onBackProgressed = { receivedEvents += it },
                onBackCancelled = { receivedEvents += "Cancel" },
                onBack = { receivedEvents += "Back" },
            )
        )

        val predictiveBackDispatcher = assertNotNull(dispatcher.startPredictiveBack(startEvent))
        predictiveBackDispatcher.progress(progressEvent1)
        predictiveBackDispatcher.progress(progressEvent2)
        predictiveBackDispatcher.cancel()

        assertContentEquals(listOf(startEvent, progressEvent1, progressEvent2, "Cancel"), receivedEvents)
    }

    private fun callback(
        isEnabled: Boolean = true,
        priority: Int = 0,
        onBack: () -> Unit = {},
    ): BackCallback =
        BackCallback(isEnabled = isEnabled, priority = priority, onBack = onBack)
}
