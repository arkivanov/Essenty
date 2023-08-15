package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.parcelize.darwin.encodeParcelableOrNull
import com.arkivanov.parcelize.darwin.encodeStringOrNull
import platform.Foundation.NSCoder
import platform.Foundation.encodeBool
import platform.Foundation.encodeDouble
import platform.Foundation.encodeFloat
import platform.Foundation.encodeInt32
import platform.Foundation.encodeInt64

/**
 * Allows deserializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 * Deserialization is performed via [platform.Foundation.NSCoder].
 */
@ExperimentalEssentyApi
actual class ParcelWriter internal constructor(internal val coder: NSCoder) {
    private var key = 0

    fun nextKey(): String = "parcel-key-${key++}"
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeBoolean(value: Boolean) {
    coder.encodeBool(value = value, forKey = nextKey())
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeInt(value: Int) {
    coder.encodeInt32(value = value, forKey = nextKey())
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeLong(value: Long) {
    coder.encodeInt64(value = value, forKey = nextKey())
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeFloat(value: Float) {
    coder.encodeFloat(value = value, forKey = nextKey())
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeDouble(value: Double) {
    coder.encodeDouble(value = value, forKey = nextKey())
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeStringOrNull(value: String?) {
    coder.encodeStringOrNull(value = value, key = nextKey())
}

@ExperimentalEssentyApi
actual fun ParcelWriter.writeParcelableOrNull(value: Parcelable?) {
    coder.encodeParcelableOrNull(value = value, key = nextKey())
}
