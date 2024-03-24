package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.SimpleInstance

/**
 * Returns a previously stored [InstanceKeeper.Instance] of type [T] with the given [key],
 * or creates and stores a new one if it doesn't exist.
 */
@Deprecated(
    message = "Please use provide(key, factory) instead.",
    level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith(
        expression = "this.provide(key, factory)",
        imports = ["com.arkivanov.essenty.instancekeeper.provide"],
    ),
)
inline fun <T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(key: Any, factory: () -> T): T =
    provide(key, factory)

/**
 * Returns a previously stored [InstanceKeeper.Instance] of type [T] with the given [key],
 * or creates and stores a new one if it doesn't exist.
 */
inline fun <T : InstanceKeeper.Instance> InstanceKeeper.provide(key: Any, factory: () -> T): T {
    @Suppress("UNCHECKED_CAST") // Assuming the type per key is always the same
    var instance: T? = get(key) as T?
    if (instance == null) {
        instance = factory()
        put(key, instance)
    }

    return instance
}

/**
 * Lazily returns a previously stored [InstanceKeeper.Instance] of type [T] with the given [key],
 * or creates and stores a new one if it doesn't exist.
 */
inline fun <T : InstanceKeeper.Instance> InstanceKeeper.providing(key: Any, crossinline factory: () -> T): Lazy<T> =
    lazy { provide(key, factory) }

/**
 * Returns a previously stored [InstanceKeeper.Instance] of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `T::class` as key.
 */
@Deprecated(
    message = "Please use provide(factory) instead.",
    level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith(
        expression = "this.provide(factory)",
        imports = ["com.arkivanov.essenty.instancekeeper.provide"],
    ),
)
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(factory: () -> T): T =
    provide(factory = factory)

/**
 * Returns a previously stored [InstanceKeeper.Instance] of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `T::class` as key.
 */
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.provide(factory: () -> T): T =
    provide(key = T::class, factory = factory)

/**
 * Lazily returns a previously stored [InstanceKeeper.Instance] of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `T::class` as key.
 */
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.providing(crossinline factory: () -> T): Lazy<T> =
    lazy { provide(factory = factory) }

/**
 * Returns a previously stored instance of type [T] with the given key,
 * or creates and stores a new one if it doesn't exist.
 *
 * This overload is for simple cases when instance destroying is not required.
 */
@Deprecated(
    message = "Please use provideSimple(key, factory) instead.",
    level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith(
        expression = "this.provideSimple(key, factory)",
        imports = ["com.arkivanov.essenty.instancekeeper.provideSimple"],
    ),
)
inline fun <T> InstanceKeeper.getOrCreateSimple(key: Any, factory: () -> T): T =
    provideSimple(key = key, factory = factory)

/**
 * Returns a previously stored instance of type [T] with the given key,
 * or creates and stores a new one if it doesn't exist.
 *
 * This overload is for simple cases when instance destroying is not required.
 */
inline fun <T> InstanceKeeper.provideSimple(key: Any, factory: () -> T): T =
    provide(key = key) { SimpleInstance(factory()) }
        .instance

/**
 * Lazily returns a previously stored instance of type [T] with the given [key],
 * or creates and stores a new one if it doesn't exist.
 *
 * This overload is for simple cases when instance destroying is not required.
 */
inline fun <T> InstanceKeeper.providingSimple(key: Any, crossinline factory: () -> T): Lazy<T> =
    lazy { provideSimple(key, factory) }

/**
 * Returns a previously stored instance of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `T::class` as key.
 *
 * This overload is for simple cases when instance destroying is not required.
 */
@Deprecated(
    message = "Please use provideSimple(factory) instead.",
    level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith(
        expression = "this.provideSimple(factory)",
        imports = ["com.arkivanov.essenty.instancekeeper.provideSimple"],
    ),
)
inline fun <reified T> InstanceKeeper.getOrCreateSimple(factory: () -> T): T =
    provideSimple(factory = factory)

/**
 * Returns a previously stored instance of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `T::class` as key.
 *
 * This overload is for simple cases when instance destroying is not required.
 */
inline fun <reified T> InstanceKeeper.provideSimple(factory: () -> T): T =
    provideSimple(key = T::class, factory = factory)

/**
 * Lazily returns a previously stored instance of type [T],
 * or creates and stores a new one if it doesn't exist. Uses `T::class` as key.
 *
 * This overload is for simple cases when instance destroying is not required.
 */
inline fun <reified T> InstanceKeeper.providingSimple(crossinline factory: () -> T): Lazy<T> =
    lazy { provideSimple(factory) }
