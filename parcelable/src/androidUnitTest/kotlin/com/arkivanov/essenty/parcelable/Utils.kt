package com.arkivanov.essenty.parcelable

import android.os.Parcel

internal inline fun <reified T : Parcelable> T.serializeAndDeserialize(): T {
    val parcel = Parcel.obtain()
    try {
        parcel.writeParcelable(this, 0)
        val bytes = parcel.marshall()
        parcel.unmarshall(bytes, 0, bytes.size)
        parcel.setDataPosition(0)

        @Suppress("DEPRECATION")
        return requireNotNull(parcel.readParcelable(T::class.java.classLoader))
    } finally {
        parcel.recycle()
    }
}
