package com.arkivanov.essenty.statekeeper

import kotlin.js.JsName

/**
 * Represents a savable [StateKeeper].
 */
interface StateKeeperDispatcher : StateKeeper {

    /**
     * Calls all registered `suppliers` and saves the data into a [SerializableContainer].
     */
    fun save(): SerializableContainer
}

/**
 * Creates a default implementation of [StateKeeperDispatcher] with the provided [savedState].
 */
@JsName("stateKeeperDispatcher")
fun StateKeeperDispatcher(savedState: SerializableContainer? = null): StateKeeperDispatcher =
    DefaultStateKeeperDispatcher(savedState)
