package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

/**
 * Allows deserializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 *
 * On Android, deserialization is performed via `android.os.Parcel`.
 *
 * On Darwin (Apple) targets, deserialization is performed via `platform.Foundation.NSCoder`.
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect class ParcelWriter

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeBoolean(value: Boolean)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeInt(value: Int)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeLong(value: Long)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeFloat(value: Float)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeDouble(value: Double)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeStringOrNull(value: String?)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeParcelableOrNull(value: Parcelable?)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelWriter.writeByteArray(value: ByteArray)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
fun ParcelWriter.writeString(value: String) {
    writeStringOrNull(value = value)
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
fun ParcelWriter.writeParcelable(value: Parcelable) {
    writeParcelableOrNull(value = value)
}
