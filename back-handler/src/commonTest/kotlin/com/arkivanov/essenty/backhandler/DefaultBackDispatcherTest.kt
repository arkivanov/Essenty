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
    fun GIVEN_callback_not_registered_WHEN_startPredictiveBack_THEN_returns_false() {
        val result = dispatcher.startPredictiveBack(BackEvent())

        assertFalse(result)
    }

    @Test
    fun GIVEN_disabled_callback_registered_WHEN_startPredictiveBack_THEN_returns_false() {
        dispatcher.register(callback = callback(isEnabled = false))

        val result = dispatcher.startPredictiveBack(BackEvent())

        assertFalse(result)
    }

    @Test
    fun GIVEN_enabled_callback_registered_WHEN_startPredictiveBack_THEN_returns_true() {
        dispatcher.register(callback = callback(isEnabled = true))

        val result = dispatcher.startPredictiveBack(BackEvent())

        assertTrue(result)
    }

    @Test
    fun WHEN_progress_with_back_THEN_callbacks_called() {
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

        dispatcher.startPredictiveBack(startEvent)
        dispatcher.progressPredictiveBack(progressEvent1)
        dispatcher.progressPredictiveBack(progressEvent2)
        dispatcher.back()

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

        dispatcher.startPredictiveBack(startEvent)
        dispatcher.progressPredictiveBack(progressEvent1)
        dispatcher.progressPredictiveBack(progressEvent2)
        dispatcher.cancelPredictiveBack()

        assertContentEquals(listOf(startEvent, progressEvent1, progressEvent2, "Cancel"), receivedEvents)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_WHEN_enabled_callback_registered_THEN_listener_called_with_true() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }

        dispatcher.register(callback(isEnabled = true, onBack = {}))

        assertContentEquals(listOf(true), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_WHEN_disabled_callback_registered_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }

        dispatcher.register(callback(isEnabled = false, onBack = {}))

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_enabled_callback_registered_and_EnabledChanged_listener_added_WHEN_callback_unregistered_THEN_listener_called_with_false() {
        val events = ArrayList<Boolean>()
        val callback = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback)
        dispatcher.addEnabledChangedListener { events += it }

        dispatcher.unregister(callback)

        assertContentEquals(listOf(false), events)
    }

    @Test
    fun GIVEN_disabled_callback_registered_and_EnabledChanged_listener_added_WHEN_callback_unregistered_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        val callback = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback)
        dispatcher.addEnabledChangedListener { events += it }

        dispatcher.unregister(callback)

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_disabled_callback_registered_and_EnabledChanged_listener_added_WHEN_callback_enabled_THEN_listener_called_with_true() {
        val events = ArrayList<Boolean>()
        val callback = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback)
        dispatcher.addEnabledChangedListener { events += it }

        callback.isEnabled = true

        assertContentEquals(listOf(true), events)
    }

    @Test
    fun GIVEN_enabled_callback_registered_and_EnabledChanged_listener_added_WHEN_callback_disabled_THEN_listener_called_with_false() {
        val events = ArrayList<Boolean>()
        val callback = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback)
        dispatcher.addEnabledChangedListener { events += it }

        callback.isEnabled = false

        assertContentEquals(listOf(false), events)
    }

    @Test
    fun GIVEN_two_disabled_callback_registered_and_EnabledChanged_listener_added_WHEN_one_callback_enabled_THEN_listener_called_with_true() {
        val events = ArrayList<Boolean>()
        val callback1 = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(callback(isEnabled = false, onBack = {}))
        dispatcher.addEnabledChangedListener { events += it }

        callback1.isEnabled = true

        assertContentEquals(listOf(true), events)
    }

    @Test
    fun GIVEN_two_enabled_callback_registered_and_EnabledChanged_listener_added_WHEN_one_callback_disabled_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        val callback1 = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(callback(isEnabled = true, onBack = {}))
        dispatcher.addEnabledChangedListener { events += it }

        callback1.isEnabled = false

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_one_disabled_and_one_enabled_callback_registered_and_EnabledChanged_listener_added_WHEN_callback_enabled_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        val callback1 = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))
        dispatcher.addEnabledChangedListener { events += it }

        callback1.isEnabled = true

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_one_disabled_and_one_enabled_callback_registered_and_EnabledChanged_listener_added_WHEN_callback_disabled_THEN_listener_called_with_false() {
        val events = ArrayList<Boolean>()
        val callback1 = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))
        dispatcher.addEnabledChangedListener { events += it }

        callback1.isEnabled = false

        assertContentEquals(listOf(false), events)
    }

    @Test
    fun GIVEN_one_disabled_and_one_enabled_callback_registered_and_EnabledChanged_listener_added_WHEN_enabled_callback_unregistered_THEN_listener_called_with_false() {
        val events = ArrayList<Boolean>()
        val callback1 = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))
        dispatcher.addEnabledChangedListener { events += it }

        dispatcher.unregister(callback1)

        assertContentEquals(listOf(false), events)
    }

    @Test
    fun GIVEN_one_disabled_and_one_enabled_callback_registered_and_EnabledChanged_listener_added_WHEN_disabled_callback_unregistered_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        val callback1 = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))
        dispatcher.addEnabledChangedListener { events += it }

        dispatcher.unregister(callback1)

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_disabled_callback_registered_WHEN_callback_enabled_THEN_listener_called_with_true() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback)
        events.clear()

        callback.isEnabled = true

        assertContentEquals(listOf(true), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_enabled_callback_registered_WHEN_callback_disabled_THEN_listener_called_with_false() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback)
        events.clear()

        callback.isEnabled = false

        assertContentEquals(listOf(false), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_two_disabled_callback_registered_WHEN_one_callback_enabled_THEN_listener_called_with_true() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback1 = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(callback(isEnabled = false, onBack = {}))
        events.clear()

        callback1.isEnabled = true

        assertContentEquals(listOf(true), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_two_enabled_callback_registered_WHEN_one_callback_disabled_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback1 = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(callback(isEnabled = true, onBack = {}))
        events.clear()

        callback1.isEnabled = false

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_one_disabled_and_one_enabled_callback_registered_WHEN_callback_enabled_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback1 = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))
        events.clear()

        callback1.isEnabled = true

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_one_disabled_and_one_enabled_callback_registered_WHEN_callback_disabled_THEN_listener_called_with_false() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback1 = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))
        events.clear()

        callback1.isEnabled = false

        assertContentEquals(listOf(false), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_one_disabled_and_one_enabled_callback_registered_WHEN_enabled_callback_unregistered_THEN_listener_called_with_false() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback1 = callback(isEnabled = true, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))
        events.clear()

        dispatcher.unregister(callback1)

        assertContentEquals(listOf(false), events)
    }

    @Test
    fun GIVEN_EnabledChanged_listener_added_and_one_disabled_and_one_enabled_callback_registered_WHEN_disabled_callback_unregistered_THEN_listener_not_called() {
        val events = ArrayList<Boolean>()
        dispatcher.addEnabledChangedListener { events += it }
        val callback1 = callback(isEnabled = false, onBack = {})
        dispatcher.register(callback1)
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))
        events.clear()

        dispatcher.unregister(callback1)

        assertContentEquals(emptyList(), events)
    }

    @Test
    fun GIVEN_callback_not_registered_WHEN_isRegistered_THEN_returns_false() {
        val isRegistered = dispatcher.isRegistered(callback())

        assertFalse(isRegistered)
    }

    @Test
    fun GIVEN_callback_registered_WHEN_isRegistered_for_another_callback_THEN_returns_false() {
        dispatcher.register(callback())

        val isRegistered = dispatcher.isRegistered(callback())

        assertFalse(isRegistered)
    }

    @Test
    fun GIVEN_callback_registered_WHEN_isRegistered_for_same_callback_THEN_returns_true() {
        val callback = callback()
        dispatcher.register(callback)

        val isRegistered = dispatcher.isRegistered(callback)

        assertTrue(isRegistered)
    }

    private fun callback(
        isEnabled: Boolean = true,
        priority: Int = 0,
        onBack: () -> Unit = {},
    ): BackCallback =
        BackCallback(isEnabled = isEnabled, priority = priority, onBack = onBack)
}
