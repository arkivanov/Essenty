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
    AndroidBackHandler().also {
        onBackPressedDispatcher.addCallback(it.onBackPressedCallback)
    }

/**
 * Creates a new instance of [BackHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher]
 * only when the [LifecycleOwner]'s Lifecycle is [STARTED][androidx.lifecycle.Lifecycle.State.STARTED].
 */
fun BackHandler(
    onBackPressedDispatcher: OnBackPressedDispatcher,
    lifecycleOwner: LifecycleOwner,
): BackHandler =
    AndroidBackHandler().also {
        onBackPressedDispatcher.addCallback(lifecycleOwner, it.onBackPressedCallback)
    }

/**
 * Creates a new instance of [BackHandler] and attaches it to the AndroidX [OnBackPressedDispatcher].
 */
fun OnBackPressedDispatcherOwner.backHandler(): BackHandler =
    BackHandler(onBackPressedDispatcher = onBackPressedDispatcher)

private class AndroidBackHandler(
    private val dispatcher: BackDispatcher = BackDispatcher()
) : BackHandler by dispatcher {

    val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(enabled = dispatcher.isEnabled) {
            private var predictiveBackDispatcher: BackDispatcher.PredictiveBackDispatcher? = null

            override fun handleOnBackPressed() {
                dispatcher.back()
                predictiveBackDispatcher = null
            }

            override fun handleOnBackStarted(backEvent: BackEventCompat) {
                predictiveBackDispatcher = dispatcher.startPredictiveBack(backEvent.toEssentyBackEvent())
            }

            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
                predictiveBackDispatcher?.progress(backEvent.toEssentyBackEvent())
            }

            override fun handleOnBackCancelled() {
                predictiveBackDispatcher?.cancel()
                predictiveBackDispatcher = null
            }
        }

    init {
        dispatcher.addEnabledChangedListener { onBackPressedCallback.isEnabled = it }
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
