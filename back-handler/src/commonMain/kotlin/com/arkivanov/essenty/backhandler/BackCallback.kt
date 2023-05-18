package com.arkivanov.essenty.backhandler

import kotlin.properties.Delegates

/**
 * A callback for back button handling.
 *
 * @param isEnabled the initial enabled state of the callback.
 * @param priority determines the order of callback execution.
 * When calling, callbacks are sorted in ascending order first by priority and then by index,
 * the last enabled callback gets called.
 */
abstract class BackCallback(
    isEnabled: Boolean = true,
    var priority: Int = PRIORITY_DEFAULT,
) {

    @Deprecated(message = "For binary compatibility", level = DeprecationLevel.HIDDEN)
    constructor(isEnabled: Boolean) : this(isEnabled = isEnabled)

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

    companion object {
        const val PRIORITY_DEFAULT: Int = 0
    }
}

fun BackCallback(
    isEnabled: Boolean = true,
    priority: Int = 0,
    onBack: () -> Unit,
): BackCallback =
    object : BackCallback(isEnabled = isEnabled, priority = priority) {
        override fun onBack() {
            onBack.invoke()
        }
    }

@Deprecated(message = "For binary compatibility", level = DeprecationLevel.HIDDEN)
fun BackCallback(
    isEnabled: Boolean,
    onBack: () -> Unit,
): BackCallback =
    BackCallback(isEnabled = isEnabled, onBack = onBack)
