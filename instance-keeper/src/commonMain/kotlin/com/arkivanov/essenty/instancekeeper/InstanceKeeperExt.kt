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
