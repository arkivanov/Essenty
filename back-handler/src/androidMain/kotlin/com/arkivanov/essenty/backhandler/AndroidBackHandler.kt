package com.arkivanov.essenty.backhandler

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.lifecycle.LifecycleOwner

/**
 * Creates a new instance of [BackHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher].
 */
fun BackHandler(onBackPressedDispatcher: OnBackPressedDispatcher): BackHandler =
    AndroidBackHandler(
        addCallback = { onBackPressed ->
            onBackPressedDispatcher.addCallback(
                enabled = false,
                onBackPressed = onBackPressed,
            )
        }
    )

/**
 * Creates a new instance of [BackHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher].
 */
fun BackHandler(
    onBackPressedDispatcher: OnBackPressedDispatcher,
    lifecycleOwner: LifecycleOwner,
): BackHandler =
    AndroidBackHandler(
        addCallback = { onBackPressed ->
            onBackPressedDispatcher.addCallback(
                owner = lifecycleOwner,
                enabled = false,
                onBackPressed = onBackPressed,
            )
        }
    )

/**
 * Creates a new instance of [BackHandler] and attaches it to the AndroidX [OnBackPressedDispatcher].
 */
fun OnBackPressedDispatcherOwner.backHandler(): BackHandler =
    BackHandler(onBackPressedDispatcher = onBackPressedDispatcher)

private class AndroidBackHandler(
    addCallback: (onBackPressed: OnBackPressedCallback.() -> Unit) -> OnBackPressedCallback,
) : BackHandler {

    private var set = emptySet<BackCallback>()
    private val delegateCallback = addCallback { set.call() }
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
