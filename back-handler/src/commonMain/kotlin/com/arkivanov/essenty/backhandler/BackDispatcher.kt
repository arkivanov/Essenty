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

    fun addEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit)

    fun removeEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit)

    /**
     * Iterates through all registered callbacks in reverse order and triggers the first one enabled.
     *
     * @return `true` if any handler was triggered, `false` otherwise.
     */
    fun back(): Boolean

    /**
     * Starts handling the predictive back gesture. Picks one of the enabled callback (if any)
     * that will be handling the gesture and calls [BackCallback.onBackStarted].
     */
    fun startPredictiveBack(backEvent: BackEvent): PredictiveBackDispatcher? = null

    /**
     * Dispatches predictive back gesture events to the callback selected by [startPredictiveBack].
     */
    interface PredictiveBackDispatcher {

        /**
         * Calls [BackCallback.onBackProgressed] on the selected callback.
         */
        fun progress(backEvent: BackEvent)

        /**
         * Calls [BackCallback.onBack] on the selected callback.
         */
        fun finish()

        /**
         * Calls [BackCallback.onBackCancelled] on the selected callback.
         */
        fun cancel()
    }
}

/**
 * Creates and returns a default implementation of [BackDispatcher].
 */
@JsName("backDispatcher")
fun BackDispatcher(): BackDispatcher =
    DefaultBackDispatcher()
