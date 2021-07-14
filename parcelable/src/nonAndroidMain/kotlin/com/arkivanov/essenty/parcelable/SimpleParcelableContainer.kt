package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ensureNeverFrozen
import kotlin.reflect.KClass

internal class SimpleParcelableContainer() : ParcelableContainer {

    init {
        ensureNeverFrozen()
    }

    private var value: Any? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : Parcelable> consume(clazz: KClass<out T>): T? =
        (value as T?).also {
            value = null
        }

    override fun set(value: Parcelable?) {
        this.value = value
    }
}
