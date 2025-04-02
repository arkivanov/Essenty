package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.SimpleInstance
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.typeOf

/**
 * Returns a previously stored [InstanceKeeper.Instance] of type [T] with the given key,
 * or creates and stores a new one if it doesn't exist.
 */
inline fun <T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(key: Any, factory: () -> T): T {
    @Suppress("UNCHECKED_CAST") // Assuming the type per key is always the same
    var instance: T? = get(key) as T?
    if (instance == null) {
        instance = factory()
        put(key, instance)
    }

    return instance
}

/**
 * Returns a previously stored [InstanceKeeper.Instance] of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `typeOf<T>()` as key.
 *
 * Deprecated. Using `getOrCreate` without the `key` parameter may crash when [T]
 * refers to a type with type (generic) parameters (e.g. a `StateFlow>`) and R8 Full Mode enabled.
 * See [KT-42913](https://youtrack.jetbrains.com/issue/KT-42913) for more information.
 */
@Deprecated(
    message = "Using getOrCreate without the key parameter is unsafe. Please use the other variant.",
    replaceWith = ReplaceWith(
        expression = "getOrCreate(key = , factory = factory)",
    )
)
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(factory: () -> T): T =
    getOrCreate(key = typeOf<T>(), factory = factory)

/**
 * Returns a previously stored [AutoCloseable] instance of type [T] with the given [key],
 * or creates and stores a new one if it doesn't exist.
 *
 * @param key a key to store and retrieve the instance.
 * @param factory a function creating a new instance of type [T].
 */
inline fun <T : AutoCloseable> InstanceKeeper.getOrCreateCloseable(key: Any, factory: () -> T): T =
    getOrCreate(key = key) {
        val instance = factory()

        object : InstanceKeeper.Instance {
            val instance: T = instance

            override fun onDestroy() {
                instance.close()
            }
        }
    }.instance

/**
 * Returns a previously stored [AutoCloseable] instance of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `typeOf<T>()` as key.
 *
 * Deprecated. Using `getOrCreateCloseable` without the `key` parameter may crash when [T]
 * refers to a type with type (generic) parameters (e.g. a `StateFlow>`) and R8 Full Mode enabled.
 * See [KT-42913](https://youtrack.jetbrains.com/issue/KT-42913) for more information.
 *
 * @param factory a function creating a new instance of type [T].
 */
@Deprecated(
    message = "Using getOrCreateCloseable without the key parameter is unsafe. Please use the other variant.",
    replaceWith = ReplaceWith(
        expression = "getOrCreateCloseable(key = , factory = factory)",
    )
)
inline fun <reified T : AutoCloseable> InstanceKeeper.getOrCreateCloseable(factory: () -> T): T =
    getOrCreateCloseable(key = typeOf<T>(), factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreate].
 */
inline fun <T : InstanceKeeper.Instance> InstanceKeeperOwner.retainedInstance(key: Any, factory: () -> T): T =
    instanceKeeper.getOrCreate(key = key, factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreate].
 *
 * Deprecated. Using `retainedInstance` without the `key` parameter may crash when [T]
 * refers to a type with type (generic) parameters (e.g. a `StateFlow>`) and R8 Full Mode enabled.
 * See [KT-42913](https://youtrack.jetbrains.com/issue/KT-42913) for more information.
 */
@Deprecated(
    message = "Using retainedInstance without the key parameter is unsafe. Please use the other variant.",
    replaceWith = ReplaceWith(
        expression = "retainedInstance(key = , factory = factory)",
    )
)
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeperOwner.retainedInstance(factory: () -> T): T =
    instanceKeeper.getOrCreate(key = typeOf<T>(), factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreateCloseable].
 */
inline fun <T : AutoCloseable> InstanceKeeperOwner.retainedCloseable(key: Any, factory: () -> T): T =
    instanceKeeper.getOrCreateCloseable(key = key, factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreateCloseable].
 *
 * Deprecated. Using `retainedCloseable` without the `key` parameter may crash when [T]
 * refers to a type with type (generic) parameters (e.g. a `StateFlow>`) and R8 Full Mode enabled.
 * See [KT-42913](https://youtrack.jetbrains.com/issue/KT-42913) for more information.
 */
@Deprecated(
    message = "Using retainedCloseable without the key parameter is unsafe. Please use the other variant.",
    replaceWith = ReplaceWith(
        expression = "retainedCloseable(key = , factory = factory)",
    )
)
inline fun <reified T : AutoCloseable> InstanceKeeperOwner.retainedCloseable(factory: () -> T): T =
    instanceKeeper.getOrCreateCloseable(key = typeOf<T>(), factory = factory)

/**
 * Returns a previously stored instance of type [T] with the given key,
 * or creates and stores a new one if it doesn't exist.
 *
 * This overload is for simple cases when instance destroying is not required.
 */
inline fun <T> InstanceKeeper.getOrCreateSimple(key: Any, factory: () -> T): T =
    getOrCreate(key = key) { SimpleInstance(factory()) }
        .instance

/**
 * Returns a previously stored instance of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `typeOf<T>()` as key.
 *
 * This overload is for simple cases when instance destroying is not required.
 *
 * Deprecated. Using `getOrCreateSimple` without the `key` parameter may crash when [T]
 * refers to a type with type (generic) parameters (e.g. a `StateFlow>`) and R8 Full Mode enabled.
 * See [KT-42913](https://youtrack.jetbrains.com/issue/KT-42913) for more information.
 */
@Deprecated(
    message = "Using getOrCreateSimple without the key parameter is unsafe. Please use the other variant.",
    replaceWith = ReplaceWith(
        expression = "getOrCreateSimple(key = , factory = factory)",
    )
)
inline fun <reified T> InstanceKeeper.getOrCreateSimple(factory: () -> T): T =
    getOrCreateSimple(key = typeOf<T>(), factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreateSimple].
 */
inline fun <T> InstanceKeeperOwner.retainedSimpleInstance(key: Any, factory: () -> T): T =
    instanceKeeper.getOrCreateSimple(key = key, factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreateSimple].
 *
 * Deprecated. Using `retainedSimpleInstance` without the `key` parameter may crash when [T]
 * refers to a type with type (generic) parameters (e.g. a `StateFlow>`) and R8 Full Mode enabled.
 * See [KT-42913](https://youtrack.jetbrains.com/issue/KT-42913) for more information.
 */
@Deprecated(
    message = "Using retainedSimpleInstance without the key parameter is unsafe. Please use the other variant.",
    replaceWith = ReplaceWith(
        expression = "retainedSimpleInstance(key = , factory = factory)",
    )
)
inline fun <reified T> InstanceKeeperOwner.retainedSimpleInstance(factory: () -> T): T =
    instanceKeeper.getOrCreateSimple(key = typeOf<T>(), factory = factory)

/**
 * Helper function for creating a
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically retained using [InstanceKeeper].
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeper
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
 * import com.arkivanov.essenty.instancekeeper.retainingInstance
 *
 * class SomeLogic(override val instanceKeeper: InstanceKeeper) : InstanceKeeperOwner {
 *     private val viewModel by retainingInstance { RetainedViewModel() }
 *
 *     private class RetainedViewModel : InstanceKeeper.Instance {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param key an optional key for storing and retrieving the instance. If not provided, then the
 * property name is used as a key.
 * @param factory a function creating a new instance of type [T].
 * @param T a type of the property, extends [InstanceKeeper.Instance].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 * @see getOrCreate
 */
@ExperimentalInstanceKeeperApi
inline fun <T : InstanceKeeper.Instance> InstanceKeeper.retainingInstance(
    key: Any? = null,
    crossinline factory: () -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val instance = getOrCreate(key = key ?: "RETAINING_INSTANCE_${property.name}", factory = factory)
        ReadOnlyProperty { _, _ -> instance }
    }

/**
 * Helper function for creating a
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically retained using [InstanceKeeper].
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeper
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
 * import com.arkivanov.essenty.instancekeeper.retainingInstance
 *
 * class SomeLogic(override val instanceKeeper: InstanceKeeper) : InstanceKeeperOwner {
 *     private val viewModel by retainingInstance { RetainedViewModel() }
 *
 *     private class RetainedViewModel : InstanceKeeper.Instance {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param key an optional key for storing and retrieving the instance. If not provided, then the
 * property name is used as a key.
 * @param factory a function creating a new instance of type [T].
 * @param T a type of the property, extends [InstanceKeeper.Instance].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 * @see getOrCreate
 */
@ExperimentalInstanceKeeperApi
inline fun <T : InstanceKeeper.Instance> InstanceKeeperOwner.retainingInstance(
    key: Any? = null,
    crossinline factory: () -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    instanceKeeper.retainingInstance(key = key, factory = factory)

/**
 * Helper function for creating a
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically retained using [InstanceKeeper].
 *
 * This overload is for simple cases when instance destroying is not required.
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeper
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
 * import com.arkivanov.essenty.instancekeeper.retainingSimpleInstance
 *
 * class SomeLogic(override val instanceKeeper: InstanceKeeper) : InstanceKeeperOwner {
 *     private val viewModel by retainingSimpleInstance { RetainedClass() }
 *
 *     private class RetainedClass {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param key an optional key for storing and retrieving the instance. If not provided, then the
 * property name is used as a key.
 * @param factory a function creating a new instance of type [T].
 * @param T a type of the property.
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 * @see getOrCreateSimple
 */
@ExperimentalInstanceKeeperApi
inline fun <T> InstanceKeeper.retainingSimpleInstance(
    key: Any? = null,
    crossinline factory: () -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val instance = getOrCreateSimple(key = key ?: "RETAINING_SIMPLE_INSTANCE_${property.name}", factory = factory)
        ReadOnlyProperty { _, _ -> instance }
    }

/**
 * Helper function for creating a
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically retained using [InstanceKeeper].
 *
 * This overload is for simple cases when instance destroying is not required.
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeper
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
 * import com.arkivanov.essenty.instancekeeper.retainingSimpleInstance
 *
 * class SomeLogic(override val instanceKeeper: InstanceKeeper) : InstanceKeeperOwner {
 *     private val viewModel by retainingSimpleInstance { RetainedClass() }
 *
 *     private class RetainedClass {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param key an optional key for storing and retrieving the instance. If not provided, then the
 * property name is used as a key.
 * @param factory a function creating a new instance of type [T].
 * @param T a type of the property.
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 * @see getOrCreateSimple
 */
@ExperimentalInstanceKeeperApi
inline fun <T> InstanceKeeperOwner.retainingSimpleInstance(
    key: Any? = null,
    crossinline factory: () -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    instanceKeeper.retainingSimpleInstance(key = key, factory = factory)

/**
 * Helper function for creating a
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically retained using [InstanceKeeper].
 *
 * This overload is for simple cases when instance destroying is not required.
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeper
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
 * import com.arkivanov.essenty.instancekeeper.retainingSimpleInstance
 *
 * class SomeLogic(override val instanceKeeper: InstanceKeeper) : InstanceKeeperOwner {
 *     private val viewModel by retainingSimpleInstance { RetainedViewModel() }
 *
 *     private class RetainedViewModel : AutoCloseable {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param key an optional key for storing and retrieving the instance. If not provided, then the
 * property name is used as a key.
 * @param factory a function creating a new instance of type [T].
 * @param T a type of the property, extends [AutoCloseable].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 * @see getOrCreateCloseable
 */
@ExperimentalInstanceKeeperApi
inline fun <T : AutoCloseable> InstanceKeeper.retainingCloseable(
    key: Any? = null,
    crossinline factory: () -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val instance = getOrCreateCloseable(key = key ?: "RETAINING_CLOSEABLE_${property.name}", factory = factory)
        ReadOnlyProperty { _, _ -> instance }
    }

/**
 * Helper function for creating a
 * [delegated property]((https://kotlinlang.org/docs/delegated-properties.html)) whose value is
 * automatically retained using [InstanceKeeper].
 *
 * This overload is for simple cases when instance destroying is not required.
 *
 * Example:
 *
 * ```
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeper
 * import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
 * import com.arkivanov.essenty.instancekeeper.retainingSimpleInstance
 *
 * class SomeLogic(override val instanceKeeper: InstanceKeeper) : InstanceKeeperOwner {
 *     private val viewModel by retainingSimpleInstance { RetainedViewModel() }
 *
 *     private class RetainedViewModel : AutoCloseable {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param key an optional key for storing and retrieving the instance. If not provided, then the
 * property name is used as a key.
 * @param factory a function creating a new instance of type [T].
 * @param T a type of the property, extends [AutoCloseable].
 * @return [PropertyDelegateProvider] of type [T], typically used to define a delegated property.
 * @see getOrCreateCloseable
 */
@ExperimentalInstanceKeeperApi
inline fun <T : AutoCloseable> InstanceKeeperOwner.retainingCloseable(
    key: Any? = null,
    crossinline factory: () -> T,
): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, T>> =
    instanceKeeper.retainingCloseable(key = key, factory = factory)
