package com.arkivanov.essenty.parcelable

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import androidx.core.os.bundleOf
import kotlin.reflect.KClass

internal class AndroidParcelableContainer(
    private var bundle: Bundle? = null
) : ParcelableContainer {

    private var value: Parcelable? = null

    override fun <T : Parcelable> consume(clazz: KClass<out T>): T? {
        val consumedValue = value ?: bundle?.apply { classLoader = clazz.java.classLoader }?.getParcelable(KEY)
        value = null
        bundle = null

        @Suppress("UNCHECKED_CAST")
        return consumedValue as T?
    }

    override fun set(value: Parcelable?) {
        this.value = value
        bundle = null
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeBundle(value?.let { bundleOf(KEY to it) } ?: bundle)
    }

    companion object CREATOR : android.os.Parcelable.Creator<AndroidParcelableContainer> {
        private const val KEY = "key"

        @SuppressLint("ParcelClassLoader") // ClassLoader is set when consumed
        override fun createFromParcel(parcel: Parcel): AndroidParcelableContainer =
            AndroidParcelableContainer(parcel.readBundle())

        override fun newArray(size: Int): Array<AndroidParcelableContainer?> = arrayOfNulls(size)
    }
}
