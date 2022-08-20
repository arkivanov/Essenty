package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable
import kotlin.reflect.KClass

/**
 * A key-value storage, typically used to persist data after process death or Android configuration changes.
 */
interface StateKeeper {

    /**
     * Removes and returns a previously saved value for the given [key].
     *
     * @param key a key to look up.
     * @param clazz a [KClass] of the value, used for deserialization.
     * @return the value for the given [key] or `null` if no value is found.
     */
    fun <T : Parcelable> consume(key: String, clazz: KClass<out T>): T?

    /**
     * Registers the value [supplier] to be called when it's time to persist the data.
     *
     * @param key a key to be associated with the value.
     * @param supplier a supplier of the value.
     */
    fun <T : Parcelable> register(key: String, supplier: () -> T?)

    /**
     * Unregisters a previously registered `supplier` for the given [key].
     */
    fun unregister(key: String)

    /**
     * Checks if a `supplier` is registered for the given [key].
     */
    fun isRegistered(key: String): Boolean
}
