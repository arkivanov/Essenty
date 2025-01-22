package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.KSerializer
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Helper function for creating a
 * [delegated property](https://kotlinlang.org/docs/delegated-properties.html) holding an object
 * whose state is automatically saved and restored using [StateKeeper].
 *
 * @param serializer a [KSerializer] for serializing and deserializing the state.
 * @param state a function that selects a state [S] from the resulting
 * object [T] and returns it for saving.
 * @param key an optional key for saving and restoring the state. If not provided, then the
 * property name is used as a key.
 * @param init a function that accepts the previously saved state [S] (if any) and
 * returns an object of type [T].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 */
@ExperimentalStateKeeperApi
inline fun <T, S : Any> StateKeeper.saveable(
    serializer: KSerializer<S>,
    crossinline state: (T) -> S,
    key: String? = null,
    crossinline init: (savedState: S?) -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val stateKey = key ?: "SAVEABLE_HOLDER_${property.name}"
        val result = init(consume(key = stateKey, strategy = serializer))
        register(key = stateKey, strategy = serializer) { state(result) }
        ReadOnlyProperty { _, _ -> result }
    }

/**
 * Helper function for creating a
 * [delegated property](https://kotlinlang.org/docs/delegated-properties.html) holding an object
 * whose state is automatically saved and restored using [StateKeeper].
 *
 * @param serializer a [KSerializer] for serializing and deserializing the state.
 * @param state a function that selects a state [S] from the resulting
 * object [T] and returns it for saving.
 * @param key an optional key for saving and restoring the state. If not provided, then the
 * property name is used as a key.
 * @param init a function that accepts the previously saved state [S] (if any) and
 * returns an object of type [T].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 */
@ExperimentalStateKeeperApi
inline fun <T, S : Any> StateKeeperOwner.saveable(
    serializer: KSerializer<S>,
    crossinline state: (T) -> S,
    key: String? = null,
    crossinline init: (savedState: S?) -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    stateKeeper.saveable(
        serializer = serializer,
        state = state,
        key = key,
        init = init,
    )

/**
 * Helper function for creating a mutable
 * [delegated property](https://kotlinlang.org/docs/delegated-properties.html) whose value is
 * automatically saved and restored using [StateKeeper].
 *
 * @param serializer a [KSerializer] for serializing and deserializing values of type [T].
 * @param key an optional key for saving and restoring the value. If not provided, then the
 * property name is used as a key.
 * @param init a function returning the initial value of type [T].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 */
@ExperimentalStateKeeperApi
inline fun <T : Any> StateKeeper.saveable(
    serializer: KSerializer<T>,
    key: String? = null,
    crossinline init: () -> T,
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val stateKey = key ?: "SAVEABLE_${property.name}"
        var saveable = consume(key = stateKey, strategy = serializer) ?: init()
        register(key = stateKey, strategy = serializer) { saveable }

        object : ReadWriteProperty<Any?, T> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                saveable

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                saveable = value
            }
        }
    }

/**
 * Helper function for creating a mutable
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically saved and restored using [StateKeeper].
 *
 * @param serializer a [KSerializer] for serializing and deserializing values of type [T].
 * @param key an optional key for saving and restoring the value. If not provided, then the
 * property name is used as a key.
 * @param init a function returning the initial value of type [T].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 */
@ExperimentalStateKeeperApi
inline fun <T : Any> StateKeeperOwner.saveable(
    serializer: KSerializer<T>,
    key: String? = null,
    crossinline init: () -> T,
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> =
    stateKeeper.saveable(
        serializer = serializer,
        key = key,
        init = init,
    )
