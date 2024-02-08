package com.arkivanov.essenty.backhandler

import androidx.activity.BackEventCompat
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class OnBackPressedCallbackAdapterTest {

    private val dispatcher = BackDispatcher()

    @Test
    fun WHEN_connected_with_empty_dispatcher_THEN_disabled() {
        val callback = dispatcher.connectOnBackPressedCallback()

        assertFalse(callback.isEnabled)
    }

    @Test
    fun WHEN_connected_with_not_empty_disabled_dispatcher_THEN_disabled() {
        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))

        val callback = dispatcher.connectOnBackPressedCallback()

        assertFalse(callback.isEnabled)
    }

    @Test
    fun WHEN_connected_with_not_empty_enabled_dispatcher_THEN_enabled() {
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))

        val callback = dispatcher.connectOnBackPressedCallback()

        assertTrue(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_empty_dispatcher_WHEN_disabled_callback_registered_in_dispatcher_THEN_disabled() {
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))

        assertFalse(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_empty_dispatcher_WHEN_enabled_callback_registered_in_dispatcher_THEN_enabled() {
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))

        assertTrue(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_not_empty_disabled_dispatcher_WHEN_disabled_callback_registered_in_dispatcher_THEN_disabled() {
        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))

        assertFalse(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_not_empty_disabled_dispatcher_WHEN_enabled_callback_registered_in_dispatcher_THEN_enabled() {
        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))

        assertTrue(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_not_empty_enabled_dispatcher_WHEN_disabled_callback_registered_in_dispatcher_THEN_enabled() {
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.register(BackCallback(isEnabled = false, onBack = {}))

        assertTrue(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_not_empty_enabled_dispatcher_WHEN_enabled_callback_registered_in_dispatcher_THEN_enabled() {
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))

        assertTrue(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_not_empty_disabled_dispatcher_WHEN_all_callbacks_unregistered_from_dispatcher_THEN_disabled() {
        val essentyCallback = BackCallback(isEnabled = false, onBack = {})
        dispatcher.register(essentyCallback)
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.unregister(essentyCallback)

        assertFalse(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_not_empty_enabled_dispatcher_WHEN_not_all_callbacks_unregistered_from_dispatcher_THEN_enabled() {
        val essentyCallback = BackCallback(isEnabled = true, onBack = {})
        dispatcher.register(essentyCallback)
        dispatcher.register(BackCallback(isEnabled = true, onBack = {}))
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.unregister(essentyCallback)

        assertTrue(callback.isEnabled)
    }

    @Test
    fun GIVEN_connected_with_not_empty_enabled_dispatcher_WHEN_all_callbacks_unregistered_from_dispatcher_THEN_disabled() {
        val essentyCallback = BackCallback(isEnabled = true, onBack = {})
        dispatcher.register(essentyCallback)
        val callback = dispatcher.connectOnBackPressedCallback()

        dispatcher.unregister(essentyCallback)

        assertFalse(callback.isEnabled)
    }

    @Test
    fun WHEN_progress_with_back_THEN_forwards() {
        val callback = dispatcher.connectOnBackPressedCallback()
        val receivedEvents = ArrayList<Any?>()
        dispatcher.register(
            BackCallback(
                onBackStarted = { receivedEvents += it },
                onBackProgressed = { receivedEvents += it },
                onBackCancelled = { receivedEvents += "Cancel" },
                onBack = { receivedEvents += "Back" },
            )
        )

        callback.handleOnBackStarted(BackEventCompat(progress = 0.1F, swipeEdge = BackEventCompat.EDGE_LEFT, touchX = 1F, touchY = 2F))
        callback.handleOnBackProgressed(BackEventCompat(progress = 0.2F, swipeEdge = BackEventCompat.EDGE_RIGHT, touchX = 2F, touchY = 3F))
        callback.handleOnBackProgressed(BackEventCompat(progress = 0.3F, swipeEdge = BackEventCompat.EDGE_LEFT, touchX = 3F, touchY = 4F))
        callback.handleOnBackPressed()

        assertContentEquals(
            listOf(
                BackEvent(progress = 0.1F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 1F, touchY = 2F),
                BackEvent(progress = 0.2F, swipeEdge = BackEvent.SwipeEdge.RIGHT, touchX = 2F, touchY = 3F),
                BackEvent(progress = 0.3F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 3F, touchY = 4F),
                "Back",
            ),
            receivedEvents,
        )
    }

    @Test
    fun WHEN_progress_with_cancel_THEN_forwards() {
        val callback = dispatcher.connectOnBackPressedCallback()
        val receivedEvents = ArrayList<Any?>()
        dispatcher.register(
            BackCallback(
                onBackStarted = { receivedEvents += it },
                onBackProgressed = { receivedEvents += it },
                onBackCancelled = { receivedEvents += "Cancel" },
                onBack = { receivedEvents += "Back" },
            )
        )

        callback.handleOnBackStarted(BackEventCompat(progress = 0.1F, swipeEdge = BackEventCompat.EDGE_LEFT, touchX = 1F, touchY = 2F))
        callback.handleOnBackProgressed(BackEventCompat(progress = 0.2F, swipeEdge = BackEventCompat.EDGE_RIGHT, touchX = 2F, touchY = 3F))
        callback.handleOnBackProgressed(BackEventCompat(progress = 0.3F, swipeEdge = BackEventCompat.EDGE_LEFT, touchX = 3F, touchY = 4F))
        callback.handleOnBackCancelled()

        assertContentEquals(
            listOf(
                BackEvent(progress = 0.1F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 1F, touchY = 2F),
                BackEvent(progress = 0.2F, swipeEdge = BackEvent.SwipeEdge.RIGHT, touchX = 2F, touchY = 3F),
                BackEvent(progress = 0.3F, swipeEdge = BackEvent.SwipeEdge.LEFT, touchX = 3F, touchY = 4F),
                "Cancel",
            ),
            receivedEvents,
        )
    }
}
