package com.arkivanov.essenty.backhandler

import androidx.activity.OnBackPressedDispatcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class AndroidBackHandlerWithLifecycleTest {

    private val dispatcher = OnBackPressedDispatcher()
    private val lifecycleOwner = LifecycleOwnerImpl()

    @Test
    fun GIVEN_lifecycle_created_WHEN_handler_created_THEN_hasEnabledCallbacks_returns_false() {
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        val handler = handler()
        handler.register(callback())

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_lifecycle_started_WHEN_handler_created_THEN_hasEnabledCallbacks_returns_true() {
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        val handler = handler()
        handler.register(callback())

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_handler_created_WHEN_lifecycle_started_THEN_hasEnabledCallbacks_returns_true() {
        val handler = handler()
        handler.register(callback())

        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        assertTrue(dispatcher.hasEnabledCallbacks())
    }

    @Test
    fun GIVEN_lifecycle_started_WHEN_lifecycle_stopped_THEN_hasEnabledCallbacks_returns_false() {
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        val handler = handler()
        handler.register(callback())

        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        assertFalse(dispatcher.hasEnabledCallbacks())
    }

    private fun handler(): BackHandler =
        BackHandler(
            onBackPressedDispatcher = dispatcher,
            lifecycleOwner = lifecycleOwner,
        )

    private fun callback(): BackCallback =
        BackCallback(isEnabled = true, onBack = {})

    private class LifecycleOwnerImpl : LifecycleOwner {
        override val lifecycle: LifecycleRegistry = LifecycleRegistry.createUnsafe(this)
    }
}
