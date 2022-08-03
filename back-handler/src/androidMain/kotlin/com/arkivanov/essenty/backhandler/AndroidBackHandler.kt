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

    private var set = emptySet<BackCallback>()
    private val delegateCallback = delegate.addCallback(enabled = false) { set.call() }
    private val enabledChangedListener: (Boolean) -> Unit = { onEnabledChanged() }

    override fun register(callback: BackCallback) {
        check(callback !in set) { "Callback is already registered" }

        this.set += callback
        callback.addEnabledChangedListener(enabledChangedListener)
        onEnabledChanged()
    }

    override fun unregister(callback: BackCallback) {
        check(callback in set) { "Callback is not registered" }

        callback.removeEnabledChangedListener(enabledChangedListener)
        this.set -= callback
        onEnabledChanged()
    }

    private fun onEnabledChanged() {
        delegateCallback.isEnabled = set.any(BackCallback::isEnabled)
    }
}
