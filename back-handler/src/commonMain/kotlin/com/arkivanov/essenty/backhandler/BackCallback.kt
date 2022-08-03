package com.arkivanov.essenty.backhandler

import kotlin.properties.Delegates

/**
 * A callback for back button handling.
 *
 * @param isEnabled the initial enabled state of the callback.
 */
abstract class BackCallback(
    isEnabled: Boolean = true,
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

    /**
     * Handles the back button press.
     */
    abstract fun onBack()
}

@Suppress("FunctionName")
fun BackCallback(
    isEnabled: Boolean = true,
    onBack: () -> Unit,
): BackCallback =
    object : BackCallback(isEnabled = isEnabled) {
        override fun onBack() {
            onBack.invoke()
        }
    }
