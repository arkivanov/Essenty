package com.arkivanov.essenty.backhandler

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback

/**
 * Creates a new instance of [BackHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher].
 */
fun BackHandler(onBackPressedDispatcher: OnBackPressedDispatcher): BackHandler =
    AndroidBackHandler(onBackPressedDispatcher)

/**
 * Creates a new instance of [BackHandler] and attaches it to the AndroidX [OnBackPressedDispatcher].
 */
fun OnBackPressedDispatcherOwner.backHandler(): BackHandler =
    AndroidBackHandler(onBackPressedDispatcher)

internal class AndroidBackHandler(
    delegate: OnBackPressedDispatcher,
) : BackHandler {

    private val map = mutableMapOf<() -> Unit, Boolean>()
    override val callbacks: BackHandler.Callbacks = map.asCallbacks(onChanged = ::updateParentCallback)
    private val delegateCallback = delegate.addCallback(enabled = false) { map.call() }

    private fun updateParentCallback() {
        delegateCallback.isEnabled = map.values.any { it }
    }
}
