package com.arkivanov.essenty.backhandler

/**
 * A handler for back button presses.
 */
interface BackHandler {

    /**
     * Checks whether the provided [BackCallback] is registered or not.
     */
    fun isRegistered(callback: BackCallback): Boolean

    /**
     * Registers the specified [callback] to be called when the back button is invoked.
     */
    fun register(callback: BackCallback)

    /**
     * Unregisters the specified [callback].
     */
    fun unregister(callback: BackCallback)
}
