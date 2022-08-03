package com.arkivanov.essenty.backhandler

/**
 * A handler for back button presses.
 */
interface BackHandler {

    /**
     * Registers the specified [callback] to be called when the back button is invoked.
     */
    fun register(callback: BackCallback)

    /**
     * Unregisters the specified [callback].
     */
    fun unregister(callback: BackCallback)
}
