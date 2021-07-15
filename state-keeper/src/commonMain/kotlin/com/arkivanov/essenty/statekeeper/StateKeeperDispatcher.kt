package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.ParcelableContainer
import kotlin.js.JsName

interface StateKeeperDispatcher : StateKeeper {

    fun save(): ParcelableContainer
}

@JsName("stateKeeperDispatcher")
@Suppress("FunctionName") // Factory function
fun StateKeeperDispatcher(savedState: ParcelableContainer? = null): StateKeeperDispatcher = DefaultStateKeeperDispatcher(savedState)
