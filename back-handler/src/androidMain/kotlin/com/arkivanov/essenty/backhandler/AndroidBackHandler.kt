package com.arkivanov.essenty.backhandler

import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.lifecycle.LifecycleOwner

/**
 * Creates a new instance of [BackHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher].
 */
fun BackHandler(onBackPressedDispatcher: OnBackPressedDispatcher): BackHandler =
    AndroidBackHandler(
        addCallback = onBackPressedDispatcher::addCallback,
    )

/**
 * Creates a new instance of [BackHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher]
 * only when the [LifecycleOwner]'s Lifecycle is [STARTED][androidx.lifecycle.Lifecycle.State.STARTED].
 */
fun BackHandler(
    onBackPressedDispatcher: OnBackPressedDispatcher,
    lifecycleOwner: LifecycleOwner,
): BackHandler =
    AndroidBackHandler(
        addCallback = { callback -> onBackPressedDispatcher.addCallback(lifecycleOwner, callback) },
    )

/**
 * Creates a new instance of [BackHandler] and attaches it to the AndroidX [OnBackPressedDispatcher].
 */
fun OnBackPressedDispatcherOwner.backHandler(): BackHandler =
    BackHandler(onBackPressedDispatcher = onBackPressedDispatcher)

private class AndroidBackHandler(
    addCallback: (OnBackPressedCallback) -> Unit,
) : BackHandler {

    private var set = emptySet<BackCallback>()

    private val delegateCallback =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                set.findMostImportant()?.onBack()
            }

            override fun handleOnBackStarted(backEvent: BackEventCompat) {
                set.findMostImportant()?.onBackStarted(backEvent.toEssentyBackEvent())
            }

            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
                set.findMostImportant()?.onBackProgressed(backEvent.toEssentyBackEvent())
            }

            override fun handleOnBackCancelled() {
                set.findMostImportant()?.onBackCancelled()
            }
        }

    private val enabledChangedListener: (Boolean) -> Unit = { onEnabledChanged() }

    init {
        addCallback(delegateCallback)
    }

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

    private fun BackEventCompat.toEssentyBackEvent(): BackEvent =
        BackEvent(
            progress = progress,
            swipeEdge = when (swipeEdge) {
                BackEventCompat.EDGE_LEFT -> BackEvent.SwipeEdge.LEFT
                BackEventCompat.EDGE_RIGHT -> BackEvent.SwipeEdge.RIGHT
                else -> BackEvent.SwipeEdge.UNKNOWN
            },
            touchX = touchX,
            touchY = touchY,
        )
}
