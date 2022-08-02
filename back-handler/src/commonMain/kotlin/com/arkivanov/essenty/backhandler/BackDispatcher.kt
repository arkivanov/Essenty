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
     * Iterates through all registered callbacks in reverse order and triggers the first one enabled.
     *
     * @return `true` if any handler was triggered, `false` otherwise.
     */
    fun back(): Boolean
}

/**
 * Creates and returns a default implementation of [BackDispatcher].
 */
@JsName("backDispatcher")
fun BackDispatcher(): BackDispatcher = DefaultBackDispatcher()
