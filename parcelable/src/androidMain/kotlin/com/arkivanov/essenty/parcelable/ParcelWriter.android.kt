@file:Suppress("MatchingDeclarationName")

package com.arkivanov.essenty.parcelable

import android.os.Parcel
import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi

/**
 * Allows deserializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 * Deserialization is performed via [android.os.Parcel].
 */
@ExperimentalEssentyApi
actual class ParcelWriter internal constructor(internal val parcel: Parcel)

@ExperimentalEssentyApi
actual fun ParcelWriter.writeBoolean(value: Boolean) {
    parcel.writeInt(if (value) 1 else 0)
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeInt(value: Int) {
    parcel.writeInt(value)
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeLong(value: Long) {
    parcel.writeLong(value)
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeFloat(value: Float) {
    parcel.writeFloat(value)
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeDouble(value: Double) {
    parcel.writeDouble(value)
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeStringOrNull(value: String?) {
    parcel.writeString(value)
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeParcelableOrNull(value: Parcelable?) {
    parcel.writeParcelable(value, 0)
}
