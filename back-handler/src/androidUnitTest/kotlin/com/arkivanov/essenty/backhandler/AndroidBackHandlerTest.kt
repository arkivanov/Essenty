package com.arkivanov.essenty.backhandler

import androidx.activity.BackEventCompat
import androidx.activity.BackEventCompat.Companion.EDGE_LEFT
import androidx.activity.BackEventCompat.Companion.EDGE_RIGHT
import androidx.activity.OnBackPressedDispatcher
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class AndroidBackHandlerTest {

    private val dispatcher = OnBackPressedDispatcher()
    private val handler = BackHandler(onBackPressedDispatcher = dispatcher)

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

    @Test
    fun GIVEN_enabled_callbacks_registered_with_priorities_WHEN_onBackPressed_THEN_last_callback_with_higher_priority_called() {
        val list = ArrayList<Int>()
        handler.register(callback(isEnabled = true, priority = 0) { list += 1 })
        handler.register(callback(isEnabled = true, priority = 1) { list += 2 })
        handler.register(callback(isEnabled = true, priority = 2) { list += 3 })
        handler.register(callback(isEnabled = true, priority = 1) { list += 4 })
        handler.register(callback(isEnabled = true, priority = 2) { list += 5 })
        handler.register(callback(isEnabled = true, priority = 0) { list += 6 })
        handler.register(callback(isEnabled = true, priority = 1) { list += 7 })
        handler.register(callback(isEnabled = true, priority = 0) { list += 8 })

        dispatcher.onBackPressed()

        assertContentEquals(listOf(5), list)
    }

    @Test
    fun GIVEN_enabled_callbacks_registered_with_priorities_WHEN_priority_changed_and_onBackPressed_THEN_last_callback_with_higher_priority_called() {
        val list = ArrayList<Int>()
        handler.register(callback(isEnabled = true, priority = 0) { list += 1 })
        val callback = callback(isEnabled = true, priority = 1) { list += 2 }
        handler.register(callback)
        handler.register(callback(isEnabled = true, priority = 2) { list += 3 })

        callback.priority = 3
        dispatcher.onBackPressed()

        assertContentEquals(listOf(2), list)
    }

    @Test
    fun WHEN_progress_with_back_THEN_callbacks_called() {
        val receivedEvents = ArrayList<Any?>()

        handler.register(
            BackCallback(
                onBackStarted = { receivedEvents += it },
                onBackProgressed = { receivedEvents += it },
                onBackCancelled = { receivedEvents += "Cancel" },
                onBack = { receivedEvents += "Back" },
            )
        )

        dispatcher.dispatchOnBackStarted(BackEventCompat(progress = 0.1F, swipeEdge = EDGE_LEFT, touchX = 1F, touchY = 2F))
        dispatcher.dispatchOnBackProgressed(BackEventCompat(progress = 0.2F, swipeEdge = EDGE_RIGHT, touchX = 2F, touchY = 3F))
        dispatcher.dispatchOnBackProgressed(BackEventCompat(progress = 0.3F, swipeEdge = EDGE_LEFT, touchX = 3F, touchY = 4F))
        dispatcher.onBackPressed()

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
    fun WHEN_progress_with_cancel_THEN_callbacks_called() {
        val receivedEvents = ArrayList<Any?>()

        handler.register(
            BackCallback(
                onBackStarted = { receivedEvents += it },
                onBackProgressed = { receivedEvents += it },
                onBackCancelled = { receivedEvents += "Cancel" },
                onBack = { receivedEvents += "Back" },
            )
        )

        dispatcher.dispatchOnBackStarted(BackEventCompat(progress = 0.1F, swipeEdge = EDGE_LEFT, touchX = 1F, touchY = 2F))
        dispatcher.dispatchOnBackProgressed(BackEventCompat(progress = 0.2F, swipeEdge = EDGE_RIGHT, touchX = 2F, touchY = 3F))
        dispatcher.dispatchOnBackProgressed(BackEventCompat(progress = 0.3F, swipeEdge = EDGE_LEFT, touchX = 3F, touchY = 4F))
        dispatcher.dispatchOnBackCancelled()

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

    private fun callback(
        isEnabled: Boolean = true,
        priority: Int = BackCallback.PRIORITY_DEFAULT,
        onBack: () -> Unit = {},
    ): BackCallback =
        BackCallback(
            isEnabled = isEnabled,
            priority = priority,
            onBack = onBack,
        )
}
