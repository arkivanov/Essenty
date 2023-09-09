package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE
import kotlin.reflect.KClass

/**
 * Allows serializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 *
 * On Android, serialization is performed via `android.os.Parcel`.
 *
 * On Darwin (Apple) targets, serialization is performed via `platform.Foundation.NSCoder`.
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect class ParcelReader

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelReader.readBoolean(): Boolean

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelReader.readInt(): Int

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelReader.readLong(): Long

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelReader.readFloat(): Float

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelReader.readDouble(): Double

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelReader.readStringOrNull(): String?

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun <T : Parcelable> ParcelReader.readParcelableOrNull(clazz: KClass<T>): T?

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
expect fun ParcelReader.readByteArray(): ByteArray

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
fun ParcelReader.readString(): String =
    requireNotNull(readStringOrNull())

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
fun <T : Parcelable> ParcelReader.readParcelable(clazz: KClass<T>): T =
    requireNotNull(readParcelableOrNull(clazz))

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
inline fun <reified T : Parcelable> ParcelReader.readParcelable(): T =
    readParcelable(clazz = T::class)

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
inline fun <reified T : Parcelable> ParcelReader.readParcelableOrNull(): T? =
    readParcelableOrNull(clazz = T::class)
