package com.arkivanov.essenty.instancekeeper

import kotlin.js.JsName

/**
 * Represents a destroyable [InstanceKeeper].
 */
interface InstanceKeeperDispatcher : InstanceKeeper {

    /**
     * Destroys all existing instances. Instances are not cleared, so that they can be
     * accessed later. Any new instances will be immediately destroyed.
     */
    fun destroy()
}

/**
 * Creates a default implementation of [InstanceKeeperDispatcher].
 */
@JsName("instanceKeeperDispatcher")
@Suppress("FunctionName")
fun InstanceKeeperDispatcher(): InstanceKeeperDispatcher = DefaultInstanceKeeperDispatcher()
