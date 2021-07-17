package com.arkivanov.essenty.backpressed

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner

/**
 * Creates a new instance of [BackPressedDispatcher] and attaches it to the provided AndroidX [OnBackPressedDispatcher]
 */
@Suppress("FunctionName") // Factory function
fun BackPressedDispatcher(onBackPressedDispatcher: OnBackPressedDispatcher): BackPressedDispatcher =
    DelegatedBackPressedDispatcher(onBackPressedDispatcher)

/**
 * Creates a new instance of [BackPressedDispatcher] and attaches it to the AndroidX [OnBackPressedDispatcher]
 */
fun OnBackPressedDispatcherOwner.backPressedDispatcher(): BackPressedDispatcher =
    BackPressedDispatcher(onBackPressedDispatcher)

private class DelegatedBackPressedDispatcher(
    private val delegate: OnBackPressedDispatcher
) : BackPressedDispatcher {

    private var isProcessingDelegateBackPressed = false
    private var handlers = emptySet<() -> Boolean>()

    private val callback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isProcessingDelegateBackPressed = true
                if (!handleBackPressed()) {
                    isEnabled = false
                    delegate.onBackPressed()
                    isEnabled = true
                }
                isProcessingDelegateBackPressed = false
            }

            private fun handleBackPressed(): Boolean =
                handlers.reversed().any { it() }
        }

    override fun register(handler: () -> Boolean) {
        if (handlers.isEmpty()) {
            delegate.addCallback(callback)
        }

        this.handlers += handler
    }

    override fun unregister(handler: () -> Boolean) {
        this.handlers -= handler

        if (handlers.isEmpty()) {
            callback.remove()
        }
    }

    override fun onBackPressed(): Boolean {
        if (!isProcessingDelegateBackPressed) {
            delegate.onBackPressed()
        }

        return true
    }
}
