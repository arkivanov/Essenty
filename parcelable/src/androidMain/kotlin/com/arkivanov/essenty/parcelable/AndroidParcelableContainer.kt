package com.arkivanov.essenty.parcelable

import android.os.Bundle
import kotlin.reflect.KClass

@Parcelize
internal class AndroidParcelableContainer(
    private val bundle: Bundle = Bundle()
) : ParcelableContainer {

    override fun <T : Parcelable> consume(clazz: KClass<out T>): T? {
        bundle.classLoader = clazz.java.classLoader
        val value: T? = bundle.getParcelable(KEY)
        if (value != null) {
            bundle.remove(KEY)
        }

        return value
    }

    override fun set(value: Parcelable?) {
        bundle.putParcelable(KEY, value)
    }

    private companion object {
        private const val KEY = "key"
    }
}
