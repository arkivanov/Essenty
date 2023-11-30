package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance

internal class DefaultInstanceKeeperDispatcher : InstanceKeeperDispatcher {

    private val map = HashMap<Any, Instance>()
    private var isDestroyed = false

    override fun get(key: Any): Instance? =
        map[key]

    override fun put(key: Any, instance: Instance) {
        check(key !in map) { "Another instance is already associated with the key: $key" }

        map[key] = instance

        if (isDestroyed) {
            instance.onDestroy()
        }
    }

    override fun remove(key: Any): Instance? =
        map.remove(key)

    override fun destroy() {
        if (!isDestroyed) {
            isDestroyed = true
            map.values.toList().forEach(Instance::onDestroy)
        }
    }
}
