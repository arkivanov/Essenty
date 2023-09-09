package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

actual class ParcelWriter private constructor()

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeBoolean(value: Boolean) {
    error("Not yet supported")
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeInt(value: Int) {
    error("Not yet supported")
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeLong(value: Long) {
    error("Not yet supported")
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeFloat(value: Float) {
    error("Not yet supported")
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeDouble(value: Double) {
    error("Not yet supported")
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeStringOrNull(value: String?) {
    error("Not yet supported")
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeParcelableOrNull(value: Parcelable?) {
    error("Not yet supported")
}

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelWriter.writeByteArray(value: ByteArray) {
    error("Not yet supported")
}
