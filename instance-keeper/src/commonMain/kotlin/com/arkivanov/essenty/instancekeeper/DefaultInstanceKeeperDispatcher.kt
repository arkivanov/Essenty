package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import com.arkivanov.essenty.utils.internal.ensureNeverFrozen

internal class DefaultInstanceKeeperDispatcher : InstanceKeeperDispatcher {

    init {
        ensureNeverFrozen()
    }

    private val map = HashMap<Any, Instance>()
    private var isDestroyed = false

    override fun get(key: Any): Instance? {
        checkIsNotDestroyed()
        return map[key]
    }

    override fun put(key: Any, instance: Instance) {
        checkIsNotDestroyed()
        check(key !in map) { "Another instance is already associated with the key: $key" }

        map[key] = instance
    }

    override fun remove(key: Any): Instance? {
        checkIsNotDestroyed()
        return map.remove(key)
    }

    override fun destroy() {
        checkIsNotDestroyed()
        isDestroyed = true
        map.values.forEach(Instance::onDestroy)
        map.clear()
    }

    private fun checkIsNotDestroyed() {
        check(!isDestroyed) { "InstanceKeeper is destroyed" }
    }
}
