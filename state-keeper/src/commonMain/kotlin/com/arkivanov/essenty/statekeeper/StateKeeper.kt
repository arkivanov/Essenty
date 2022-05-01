package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable
import kotlin.reflect.KClass

interface StateKeeper {

    fun <T : Parcelable> consume(key: String, clazz: KClass<out T>): T?

    fun <T : Parcelable> register(key: String, supplier: () -> T)

    fun unregister(key: String)

    fun isRegistered(key: String): Boolean
}
