package com.arkivanov.essenty.backhandler

/**
 * A handler for back button presses.
 */
interface BackHandler {

    /**
     * Provides an access to [Callbacks].
     */
    val callbacks: Callbacks

    /**
     * A collection of callbacks for back button presses.
     */
    interface Callbacks {
        /**
         * Returns the enabled state of the specified [callback], or `null` if the [callback] is not registered.
         */
        operator fun get(callback: () -> Unit): Boolean?

        /**
         * Puts the specified [callback] to the collection and sets its enabled state.
         * If the [callback] is not yet registered, it is added to the end of the collection with the specified enabled state.
         * Otherwise the position of the callback remains unchanged, but its enabled state is updated with the specified value.
         */
        fun put(callback: () -> Unit, isEnabled: Boolean)

        /**
         * Removes the specified [callback] from the collection.
         */
        fun remove(callback: () -> Unit)
    }
}

/**
 * A convenience method for [BackHandler.Callbacks.put].
 * Puts the specified [callback] to the collection and sets its enabled state to `true`.
 */
fun BackHandler.Callbacks.put(callback: () -> Unit) {
    put(callback = callback, isEnabled = true)
}

/**
 * A convenience method for indexed access.
 *
 * Example:
 *
 * ```
 * backHandler.callbacks[callback] = true|false
 * ```
 */
operator fun BackHandler.Callbacks.set(callback: () -> Unit, isEnabled: Boolean) {
    put(callback = callback, isEnabled = isEnabled)
}

/**
 * A convenience method for `plusAssign` operator.
 *
 * Example:
 *
 * ```
 * backHandler.callbacks += { ... }
 * ```
 */
operator fun BackHandler.Callbacks.plusAssign(callback: () -> Unit) {
    put(callback = callback)
}

/**
 * A convenience method for `minusAssign` operator.
 *
 * Example:
 *
 * ```
 * backHandler.callbacks -= callback
 * ```
 */
operator fun BackHandler.Callbacks.minusAssign(callback: () -> Unit) {
    remove(callback = callback)
}
