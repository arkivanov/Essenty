package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
internal class DefaultInstanceKeeperDispatcher : InstanceKeeperDispatcher {

    private val state = AtomicReference(State())

    override fun get(key: Any): Instance? =
        state.load().map[key]

    override fun put(key: Any, instance: Instance) {
        val previous =
            state.getAndUpdate { current ->
                if (key in current.map) {
                    current
                } else {
                    current.copy(map = current.map + (key to instance))
                }
            }

        check(key !in previous.map) { "Another instance is already associated with the key: $key" }

        if (previous.isDestroyed) {
            instance.onDestroy()
        }
    }

    override fun remove(key: Any): Instance? =
        state.getAndUpdate { it.copy(map = it.map - key) }.map[key]

    override fun destroy() {
        val previous = state.getAndUpdate { it.copy(isDestroyed = true) }
        if (!previous.isDestroyed) {
            previous.map.values.toList().forEach(Instance::onDestroy)
        }
    }

    private data class State(
        val map: Map<Any, Instance> = emptyMap(),
        val isDestroyed: Boolean = false,
    )
}

@OptIn(ExperimentalAtomicApi::class)
private fun <T> AtomicReference<T>.getAndUpdate(update: (T) -> T): T {
    while (true) {
        val prev = load()
        val next = update(prev)
        if (compareAndSet(prev, next)) {
            return prev
        }
    }
}
