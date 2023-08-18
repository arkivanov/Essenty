package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi

/**
 * Allows deserializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 *
 * On Android, deserialization is performed via `android.os.Parcel`.
 *
 * On Darwin (Apple) targets, deserialization is performed via `platform.Foundation.NSCoder`.
 */
@ExperimentalEssentyApi
expect class ParcelWriter

@ExperimentalEssentyApi
expect fun ParcelWriter.writeBoolean(value: Boolean)

@ExperimentalEssentyApi
expect fun ParcelWriter.writeInt(value: Int)

@ExperimentalEssentyApi
expect fun ParcelWriter.writeLong(value: Long)

@ExperimentalEssentyApi
expect fun ParcelWriter.writeFloat(value: Float)

@ExperimentalEssentyApi
expect fun ParcelWriter.writeDouble(value: Double)

@ExperimentalEssentyApi
expect fun ParcelWriter.writeStringOrNull(value: String?)

@ExperimentalEssentyApi
expect fun ParcelWriter.writeParcelableOrNull(value: Parcelable?)

@ExperimentalEssentyApi
fun ParcelWriter.writeString(value: String) {
    writeStringOrNull(value = value)
}

@ExperimentalEssentyApi
fun ParcelWriter.writeParcelable(value: Parcelable) {
    writeParcelableOrNull(value = value)
}
