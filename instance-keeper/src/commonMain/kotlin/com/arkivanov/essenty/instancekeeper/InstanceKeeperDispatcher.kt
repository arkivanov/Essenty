package com.arkivanov.essenty.instancekeeper

import kotlin.js.JsName

/**
 * Represents a destroyable [InstanceKeeper].
 *
 * The default implementation returned by [InstanceKeeperDispatcher] is thread-safe.
 */
interface InstanceKeeperDispatcher : InstanceKeeper {

    /**
     * Destroys all existing instances. Instances are not cleared, so that they can be
     * accessed later. Any new instances will be immediately destroyed via [InstanceKeeper.Instance.onDestroy].
     *
     * Calling this method more than once has no effect.
     */
    fun destroy()
}

/**
 * Creates a default thread-safe implementation of [InstanceKeeperDispatcher].
 */
@JsName("instanceKeeperDispatcher")
fun InstanceKeeperDispatcher(): InstanceKeeperDispatcher = DefaultInstanceKeeperDispatcher()
