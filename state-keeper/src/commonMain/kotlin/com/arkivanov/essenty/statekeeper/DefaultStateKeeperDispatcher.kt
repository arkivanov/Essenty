package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy

internal class DefaultStateKeeperDispatcher(
    savedState: SerializableContainer?,
) : StateKeeperDispatcher {

    private val savedState: MutableMap<String, SerializableContainer>? = savedState?.consume(strategy = SavedState.serializer())?.map
    private val suppliers = HashMap<String, Supplier<*>>()

    override fun save(): SerializableContainer {
        val map = HashMap<String, SerializableContainer>()
        suppliers.forEach { (key, supplier) ->
            supplier.toSerializableContainer()?.also { container ->
                map[key] = container
            }
        }

        return SerializableContainer(value = SavedState(map), strategy = SavedState.serializer())
    }

    private fun <T : Any> Supplier<T>.toSerializableContainer(): SerializableContainer? =
        supplier()?.let { value ->
            SerializableContainer(value = value, strategy = strategy)
        }

    override fun <T : Any> consume(key: String, strategy: DeserializationStrategy<T>): T? =
        savedState
            ?.remove(key)
            ?.consume(strategy = strategy)

    override fun <T : Any> register(key: String, strategy: SerializationStrategy<T>, supplier: () -> T?) {
        check(!isRegistered(key)) { "Another supplier is already registered with the key: $key" }
        suppliers[key] = Supplier(strategy = strategy, supplier = supplier)
    }

    override fun unregister(key: String) {
        check(isRegistered(key)) { "No supplier is registered with the key: $key" }
        suppliers -= key
    }

    override fun isRegistered(key: String): Boolean = key in suppliers

    private class Supplier<T : Any>(
        val strategy: SerializationStrategy<T>,
        val supplier: () -> T?,
    )

    @Serializable
    private class SavedState(
        val map: MutableMap<String, SerializableContainer>
    )
}
