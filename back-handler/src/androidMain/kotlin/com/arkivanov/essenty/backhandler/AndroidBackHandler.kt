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
    BackDispatcher().also { dispatcher ->
        onBackPressedDispatcher.addCallback(dispatcher.connectOnBackPressedCallback())
    }

/**
 * Creates a new instance of [BackHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher]
 * only when the [LifecycleOwner]'s Lifecycle is [STARTED][androidx.lifecycle.Lifecycle.State.STARTED].
 */
fun BackHandler(
    onBackPressedDispatcher: OnBackPressedDispatcher,
    lifecycleOwner: LifecycleOwner,
): BackHandler =
    BackDispatcher().also { dispatcher ->
        onBackPressedDispatcher.addCallback(lifecycleOwner, dispatcher.connectOnBackPressedCallback())
    }

/**
 * Creates a new instance of [BackHandler] and attaches it to the AndroidX [OnBackPressedDispatcher].
 */
fun OnBackPressedDispatcherOwner.backHandler(): BackHandler =
    BackHandler(onBackPressedDispatcher = onBackPressedDispatcher)

/**
 * Creates a new instance of [OnBackPressedCallback] and connects it with this [BackDispatcher].
 * All events from the returned [OnBackPressedCallback] are forwarded to this [BackDispatcher].
 * The enabled state from this [BackDispatcher] is forwarded to the returned [OnBackPressedCallback].
 */
fun BackDispatcher.connectOnBackPressedCallback(): OnBackPressedCallback =
    OnBackPressedCallbackAdapter(dispatcher = this)

private class OnBackPressedCallbackAdapter(
    private val dispatcher: BackDispatcher,
) : OnBackPressedCallback(enabled = dispatcher.isEnabled) {

    init {
        dispatcher.addEnabledChangedListener { isEnabled = it }
    }

    override fun handleOnBackPressed() {
        dispatcher.back()
    }

    override fun handleOnBackStarted(backEvent: BackEventCompat) {
        dispatcher.startPredictiveBack(backEvent.toEssentyBackEvent())
    }

    override fun handleOnBackProgressed(backEvent: BackEventCompat) {
        dispatcher.progressPredictiveBack(backEvent.toEssentyBackEvent())
    }

    override fun handleOnBackCancelled() {
        dispatcher.cancelPredictiveBack()
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
