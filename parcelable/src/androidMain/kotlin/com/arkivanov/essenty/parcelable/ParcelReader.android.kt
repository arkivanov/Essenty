@file:Suppress("MatchingDeclarationName")

package com.arkivanov.essenty.parcelable

import android.os.Parcel
import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE
import kotlin.reflect.KClass

/**
 * Allows serializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 * Serialization is performed via [android.os.Parcel].
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual class ParcelReader internal constructor(internal val parcel: Parcel)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readBoolean(): Boolean =
    parcel.readInt() != 0

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readInt(): Int =
    parcel.readInt()

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readLong(): Long =
    parcel.readLong()

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readFloat(): Float =
    parcel.readFloat()

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readDouble(): Double =
    parcel.readDouble()

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readStringOrNull(): String? =
    parcel.readString()

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@Suppress("DEPRECATION")
@ExperimentalEssentyApi
actual fun <T : Parcelable> ParcelReader.readParcelableOrNull(clazz: KClass<T>): T? =
    parcel.readParcelable(clazz.java.classLoader)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readByteArray(): ByteArray =
    requireNotNull(parcel.createByteArray())
