package com.arkivanov.essenty.instancekeeper

interface InstanceKeeper {

    fun get(key: Any): Instance?

    fun put(key: Any, instance: Instance)

    fun remove(key: Any): Instance?

    interface Instance {
        fun onDestroy()
    }
}
