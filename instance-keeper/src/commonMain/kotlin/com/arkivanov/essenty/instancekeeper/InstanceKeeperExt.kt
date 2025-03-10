package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.SimpleInstance
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
 */
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(factory: () -> T): T =
    getOrCreate(key = typeOf<T>(), factory = factory)

/**
 * Returns a previously stored [AutoCloseable] instance of type [T] with the given [key],
 * or creates and stores a new one if it doesn't exist.
 *
 * @param key a key to store and retrieve the instance, default value is `typeOf<T>()`.
 * @param factory a function creating a new instance of type [T].
 */
inline fun <reified T : AutoCloseable> InstanceKeeper.getOrCreateCloseable(key: Any = typeOf<T>(), factory: () -> T): T =
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
 * A convenience function for [InstanceKeeper.getOrCreate].
 */
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeperOwner.retainedInstance(key: Any, factory: () -> T): T =
    instanceKeeper.getOrCreate(key = key, factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreate].
 */
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeperOwner.retainedInstance(factory: () -> T): T =
    instanceKeeper.getOrCreate(factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreateCloseable].
 */
inline fun <reified T : AutoCloseable> InstanceKeeperOwner.retainedCloseable(key: Any = typeOf<T>(), factory: () -> T): T =
    instanceKeeper.getOrCreateCloseable(key = key, factory = factory)

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
 */
inline fun <reified T> InstanceKeeper.getOrCreateSimple(factory: () -> T): T =
    getOrCreateSimple(key = typeOf<T>(), factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreateSimple].
 */
inline fun <reified T> InstanceKeeperOwner.retainedSimpleInstance(key: Any, factory: () -> T): T =
    instanceKeeper.getOrCreateSimple(key = key, factory = factory)

/**
 * A convenience function for [InstanceKeeper.getOrCreateSimple].
 */
inline fun <reified T> InstanceKeeperOwner.retainedSimpleInstance(factory: () -> T): T =
    instanceKeeper.getOrCreateSimple(factory = factory)
