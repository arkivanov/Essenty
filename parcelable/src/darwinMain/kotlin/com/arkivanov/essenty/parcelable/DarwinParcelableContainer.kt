package com.arkivanov.essenty.parcelable

import kotlin.reflect.KClass

@Parcelize
internal data class DarwinParcelableContainer(
    private var parcelable: Parcelable? = null
) : ParcelableContainer {

    override fun <T : Parcelable> consume(clazz: KClass<out T>): T? =
        (parcelable as T?).also {
            parcelable = null
        }

    override fun set(value: Parcelable?) {
        this.parcelable = value
    }
}
