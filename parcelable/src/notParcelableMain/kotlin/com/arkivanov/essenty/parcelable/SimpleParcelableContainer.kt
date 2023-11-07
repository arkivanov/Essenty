package com.arkivanov.essenty.parcelable

import kotlin.reflect.KClass

internal class SimpleParcelableContainer : ParcelableContainer {

    private var value: Parcelable? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : Parcelable> consume(clazz: KClass<out T>): T? =
        (value as T?).also {
            value = null
        }

    override fun set(value: Parcelable?) {
        this.value = value
    }
}
