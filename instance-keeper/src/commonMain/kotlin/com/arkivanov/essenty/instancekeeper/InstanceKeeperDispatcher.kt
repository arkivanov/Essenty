package com.arkivanov.essenty.instancekeeper

import kotlin.js.JsName

/**
 * Represents a destroyable [InstanceKeeper].
 */
interface InstanceKeeperDispatcher : InstanceKeeper {

    fun destroy()
}

/**
 * Creates a default implementation of [InstanceKeeperDispatcher].
 */
@JsName("instanceKeeperDispatcher")
@Suppress("FunctionName")
fun InstanceKeeperDispatcher(): InstanceKeeperDispatcher = DefaultInstanceKeeperDispatcher()
