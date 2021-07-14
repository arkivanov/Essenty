package com.arkivanov.essenty.parcelable

import kotlin.reflect.KClass

interface ParcelableContainer : Parcelable {

    fun <T : Parcelable> consume(clazz: KClass<out T>): T?

    fun set(value: Parcelable?)
}

expect fun ParcelableContainer(value: Parcelable? = null): ParcelableContainer
