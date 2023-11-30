package com.arkivanov.essenty.instancekeeper

/**
 * A generic keyed store of [Instance] objects. Instances are destroyed at the end of the
 * [InstanceKeeper]'s scope, which is typically tied to the scope of a back stack entry.
 * E.g. instances are retained over Android configuration changes, and destroyed when the
 * corresponding back stack entry is popped.
 */
interface InstanceKeeper {

    /**
     * Returns an instance with the given [key], or `null` if no instance with the given key exists.
     */
    fun get(key: Any): Instance?

    /**
     * Stores the given [instance] with the given [key]. Throws [IllegalStateException] if another
     * instance is already registered with the given [key].
     */
    fun put(key: Any, instance: Instance)

    /**
     * Removes an instance with the given [key]. This does not destroy the instance.
     */
    fun remove(key: Any): Instance?

    /**
     * Represents a destroyable instance.
     */
    interface Instance {

        /**
         * Called at the end of the [InstanceKeeper]'s scope.
         */
        fun onDestroy() {}
    }

    /**
     * Are simple [Instance] wrapper for cases when destroying is not required.
     */
    class SimpleInstance<out T>(val instance: T) : Instance
}
