package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import com.arkivanov.essenty.utils.internal.ensureNeverFrozen

internal class DefaultInstanceKeeperDispatcher : InstanceKeeperDispatcher {

    init {
        ensureNeverFrozen()
    }

    private val map = HashMap<Any, Instance>()

    override fun get(key: Any): Instance? = map[key]

    override fun put(key: Any, instance: Instance) {
        check(key !in map) { "Another instance is already associated with the key: $key" }

        map[key] = instance
    }

    override fun destroy() {
        map.values.forEach(Instance::onDestroy)
        map.clear()
    }
}
