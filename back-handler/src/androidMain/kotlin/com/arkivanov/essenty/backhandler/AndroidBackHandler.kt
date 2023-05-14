package com.arkivanov.essenty.backhandler

import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import android.window.BackEvent as AndroidBackEvent

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

            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun handleOnBackStarted(backEvent: AndroidBackEvent) {
                set.findMostImportant()?.onBackStarted(backEvent.toBackEvent())
            }

            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun handleOnBackProgressed(backEvent: AndroidBackEvent) {
                set.findMostImportant()?.onBackProgressed(backEvent.toBackEvent())
            }

            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun AndroidBackEvent.toBackEvent(): BackEvent =
        BackEvent(
            progress = progress,
            swipeEdge = when (swipeEdge) {
                AndroidBackEvent.EDGE_LEFT -> BackEvent.SwipeEdge.LEFT
                AndroidBackEvent.EDGE_RIGHT -> BackEvent.SwipeEdge.RIGHT
                else -> BackEvent.SwipeEdge.UNKNOWN
            },
            touchX = touchX,
            touchY = touchY,
        )
}
