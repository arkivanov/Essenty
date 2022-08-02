package com.arkivanov.essenty.backhandler

import kotlin.properties.Delegates

/**
 * A handler for back button presses.
 */
interface BackHandler {

    /**
     * Registers the specified [callback] to be called when the back button is invoked.
     */
    fun register(callback: Callback)

    /**
     * Unregisters the specified [callback].
     */
    fun unregister(callback: Callback)

    /**
     * A callback for back button handling.
     *
     * @param isEnabled the initial enabled state of the callback.
     * @param callback a callback to be called when the back button is invoked.
     */
    class Callback(
        isEnabled: Boolean = true,
        val callback: () -> Unit,
    ) {
        private var enabledListeners = emptySet<(Boolean) -> Unit>()

        /**
         * Controls the enabled state of the callback.
         */
        var isEnabled: Boolean by Delegates.observable(isEnabled) { _, _, newValue ->
            enabledListeners.forEach { it(newValue) }
        }

        /**
         * Registers the specified [listener] to be called when the enabled state of the callback changes.
         */
        fun addEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit) {
            this.enabledListeners += listener
        }

        /**
         * Unregisters the specified [listener].
         */
        fun removeEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit) {
            this.enabledListeners -= listener
        }
    }
}
