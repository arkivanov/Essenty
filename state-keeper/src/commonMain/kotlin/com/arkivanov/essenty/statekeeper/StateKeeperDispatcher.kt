package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.ParcelableContainer
import kotlin.js.JsName

/**
 * Represents a savable [StateKeeper].
 */
interface StateKeeperDispatcher : StateKeeper {

    /**
     * Calls all registered `suppliers` and saves the data into a [ParcelableContainer].
     */
    fun save(): ParcelableContainer
}

/**
 * Creates a default implementation of [StateKeeperDispatcher] with the provided [savedState].
 */
@JsName("stateKeeperDispatcher")
@Suppress("FunctionName") // Factory function
fun StateKeeperDispatcher(savedState: ParcelableContainer? = null): StateKeeperDispatcher = DefaultStateKeeperDispatcher(savedState)
