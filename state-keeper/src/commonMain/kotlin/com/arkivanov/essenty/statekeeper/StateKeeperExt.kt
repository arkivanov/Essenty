package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.concurrent.Volatile
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Helper function for creating a
 * [delegated property](https://kotlinlang.org/docs/delegated-properties.html) holding an object
 * whose state is automatically saved and restored using [StateKeeper].
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.statekeeper.StateKeeper
 * import com.arkivanov.essenty.statekeeper.saveable
 * import kotlinx.serialization.Serializable
 *
 * private class SomeLogic(stateKeeper: StateKeeper) {
 *
 *     private val stateHolder by stateKeeper.saveable(
 *         serializer = State.serializer(),
 *         state = StateHolder::state,
 *     ) { savedState ->
 *         StateHolder(state = savedState ?: State())
 *     }
 *
 *     private class StateHolder(var state: State)
 *
 *     @Serializable
 *     private class State(val someValue: Int = 0)
 * }
 * ```
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
fun <T, S> StateKeeper.saveable(
    serializer: KSerializer<S>,
    state: (T) -> S,
    key: String? = null,
    init: (savedState: S?) -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val stateKey = key ?: "SAVEABLE_HOLDER_${property.name}"
        val holderSerializer = Holder.serializer(serializer)
        val result = init(consume(key = stateKey, strategy = holderSerializer)?.value)
        register(key = stateKey, strategy = holderSerializer) { Holder(state(result)) }
        ReadOnlyProperty { _, _ -> result }
    }

/**
 * Helper function for creating a
 * [delegated property](https://kotlinlang.org/docs/delegated-properties.html) holding an object
 * whose state is automatically saved and restored using [StateKeeper].
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.statekeeper.StateKeeper
 * import com.arkivanov.essenty.statekeeper.saveable
 * import kotlinx.serialization.Serializable
 *
 * private class SomeLogic(override val stateKeeper: StateKeeper) : StateKeeperOwner {
 *
 *     private val stateHolder by saveable(
 *         serializer = State.serializer(),
 *         state = StateHolder::state,
 *     ) { savedState ->
 *         StateHolder(state = savedState ?: State())
 *     }
 *
 *     private class StateHolder(var state: State)
 *
 *     @Serializable
 *     private class State(val someValue: Int = 0)
 * }
 * ```
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
fun <T, S> StateKeeperOwner.saveable(
    serializer: KSerializer<S>,
    state: (T) -> S,
    key: String? = null,
    init: (savedState: S?) -> T,
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
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.statekeeper.StateKeeper
 * import com.arkivanov.essenty.statekeeper.saveable
 * import kotlinx.serialization.Serializable
 *
 * class SomeLogic(stateKeeper: StateKeeper) {
 *     private var state: State by stateKeeper.saveable(serializer = State.serializer(), init = ::State)
 *
 *     @Serializable
 *     private class State(val someValue: Int = 0)
 * }
 * ```
 *
 * @param serializer a [KSerializer] for serializing and deserializing values of type [T].
 * @param key an optional key for saving and restoring the value. If not provided, then the
 * property name is used as a key.
 * @param init a function returning the initial value of type [T].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 */
@ExperimentalStateKeeperApi
fun <T> StateKeeper.saveable(
    serializer: KSerializer<T>,
    key: String? = null,
    init: () -> T,
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val stateKey = key ?: "SAVEABLE_${property.name}"
        val holderSerializer = Holder.serializer(serializer)
        val holder = consume(key = stateKey, strategy = holderSerializer) ?: Holder(init())
        register(key = stateKey, strategy = holderSerializer) { Holder(holder.value) }
        holder
    }

@Serializable
private class Holder<T>(@Volatile var value: T) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        this.value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

/**
 * Helper function for creating a mutable
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically saved and restored using [StateKeeper].
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.statekeeper.StateKeeper
 * import com.arkivanov.essenty.statekeeper.saveable
 * import kotlinx.serialization.Serializable
 *
 * class SomeLogic(override val stateKeeper: StateKeeper) : StateKeeperOwner {
 *     private var state: State by saveable(serializer = State.serializer(), init = ::State)
 *
 *     @Serializable
 *     private class State(val someValue: Int = 0)
 * }
 * ```
 *
 * @param serializer a [KSerializer] for serializing and deserializing values of type [T].
 * @param key an optional key for saving and restoring the value. If not provided, then the
 * property name is used as a key.
 * @param init a function returning the initial value of type [T].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 */
@ExperimentalStateKeeperApi
fun <T> StateKeeperOwner.saveable(
    serializer: KSerializer<T>,
    key: String? = null,
    init: () -> T,
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> =
    stateKeeper.saveable(
        serializer = serializer,
        key = key,
        init = init,
    )
