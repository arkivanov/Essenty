package com.arkivanov.essenty.parcelable

import kotlin.reflect.KClass

internal data class JvmParcelableContainer(
    private var parcelable: Parcelable? = null
) : ParcelableContainer {

    override fun <T : Parcelable> consume(clazz: KClass<out T>): T? =
        @Suppress("UNCHECKED_CAST")
        (parcelable as T?).also {
            parcelable = null
        }

    override fun set(value: Parcelable?) {
        this.parcelable = value
    }
}
