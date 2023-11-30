package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

/**
 * A key-value storage, typically used to persist data after process death or Android configuration changes.
 */
interface StateKeeper {

    /**
     * Removes and returns a previously saved value for the given [key].
     *
     * @param key a key to look up.
     * @param strategy a [DeserializationStrategy] for deserializing the value.
     * @return the value for the given [key] or `null` if no value is found.
     */
    fun <T : Any> consume(key: String, strategy: DeserializationStrategy<T>): T?

    /**
     * Registers the value [supplier] to be called when it's time to persist the data.
     *
     * @param key a key to be associated with the value.
     * @param strategy a [SerializationStrategy] for serializing the value.
     * @param supplier a supplier of the value.
     */
    fun <T : Any> register(key: String, strategy: SerializationStrategy<T>, supplier: () -> T?)

    /**
     * Unregisters a previously registered `supplier` for the given [key].
     */
    fun unregister(key: String)

    /**
     * Checks if a `supplier` is registered for the given [key].
     */
    fun isRegistered(key: String): Boolean
}
