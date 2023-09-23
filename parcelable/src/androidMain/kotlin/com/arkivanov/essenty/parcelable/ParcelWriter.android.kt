@file:Suppress("MatchingDeclarationName")

package com.arkivanov.essenty.parcelable

import android.os.Parcel
import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

/**
 * Allows deserializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 * Deserialization is performed via [android.os.Parcel].
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual class ParcelWriter internal constructor(internal val parcel: Parcel)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeBoolean(value: Boolean) {
    parcel.writeInt(if (value) 1 else 0)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeInt(value: Int) {
    parcel.writeInt(value)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeLong(value: Long) {
    parcel.writeLong(value)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeFloat(value: Float) {
    parcel.writeFloat(value)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeDouble(value: Double) {
    parcel.writeDouble(value)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeStringOrNull(value: String?) {
    parcel.writeString(value)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeParcelableOrNull(value: Parcelable?) {
    parcel.writeParcelable(value, 0)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeByteArray(value: ByteArray) {
    parcel.writeByteArray(value)
}
