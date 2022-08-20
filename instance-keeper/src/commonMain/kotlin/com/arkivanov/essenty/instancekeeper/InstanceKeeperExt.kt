package com.arkivanov.essenty.instancekeeper

/**
 * Returns a previously stored [InstanceKeeper.Instance] with the given key,
 * or creates and stores a new one if it doesn't exist.
 */
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(key: Any, factory: () -> T): T {
    var instance: T? = get(key) as T?
    if (instance == null) {
        instance = factory()
        put(key, instance)
    }

    return instance
}

/**
 * Returns a previously stored [InstanceKeeper.Instance] with the given key,
 * or creates and stores a new one if it doesn't exist.
 */
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(factory: () -> T): T = getOrCreate(T::class, factory)
