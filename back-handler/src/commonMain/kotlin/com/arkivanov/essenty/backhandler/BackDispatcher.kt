package com.arkivanov.essenty.backhandler

import kotlin.js.JsName

/**
 * Provides a way to manually trigger back button handlers.
 */
interface BackDispatcher : BackHandler {

    /**
     * Returns `true` if there is at least one enabled handler, `false` otherwise.
     */
    val isEnabled: Boolean

    /**
     * Adds the provided [listener], which will be called every time the enabled state of
     * this [BackDispatcher] changes.
     */
    fun addEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit)

    /**
     * Removes the provided enabled state changed [listener].
     */
    fun removeEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit)

    /**
     * If no predictive back gesture is currently in progress, finds the last enabled
     * callback with the highest priority and calls [BackCallback.onBack].
     *
     * If the predictive back gesture is currently in progress, calls [BackCallback.onBack] on
     * the previously selected callback.
     *
     * @return `true` if any callback was triggered, `false` otherwise.
     */
    fun back(): Boolean

    /**
     * Starts handling the predictive back gesture. Picks one of the enabled callback (if any)
     * that will be handling the gesture and calls [BackCallback.onBackStarted].
     *
     * @return `true` if any callback was triggered, `false` otherwise.
     */
    fun startPredictiveBack(backEvent: BackEvent): Boolean

    /**
     * Calls [BackCallback.onBackProgressed] on the previously selected callback.
     */
    fun progressPredictiveBack(backEvent: BackEvent)

    /**
     * Calls [BackCallback.onBackCancelled] on the previously selected callback.
     */
    fun cancelPredictiveBack()
}

/**
 * Creates and returns a default implementation of [BackDispatcher].
 */
@JsName("backDispatcher")
fun BackDispatcher(): BackDispatcher =
    DefaultBackDispatcher()
