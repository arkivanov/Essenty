package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import kotlin.reflect.KClass

/**
 * Allows serializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 *
 * On Android, serialization is performed via `android.os.Parcel`.
 *
 * On Darwin (Apple) targets, serialization is performed via `platform.Foundation.NSCoder`.
 */
@ExperimentalEssentyApi
expect class ParcelReader

@ExperimentalEssentyApi
expect fun ParcelReader.readBoolean(): Boolean

@ExperimentalEssentyApi
expect fun ParcelReader.readInt(): Int

@ExperimentalEssentyApi
expect fun ParcelReader.readLong(): Long

@ExperimentalEssentyApi
expect fun ParcelReader.readFloat(): Float

@ExperimentalEssentyApi
expect fun ParcelReader.readDouble(): Double

@ExperimentalEssentyApi
expect fun ParcelReader.readStringOrNull(): String?

@ExperimentalEssentyApi
expect fun <T : Parcelable> ParcelReader.readParcelableOrNull(clazz: KClass<T>): T?

@ExperimentalEssentyApi
fun ParcelReader.readString(): String =
    requireNotNull(readStringOrNull())

@ExperimentalEssentyApi
fun <T : Parcelable> ParcelReader.readParcelable(clazz: KClass<T>): T =
    requireNotNull(readParcelableOrNull(clazz))

@ExperimentalEssentyApi
inline fun <reified T : Parcelable> ParcelReader.readParcelable(): T =
    readParcelable(clazz = T::class)

@ExperimentalEssentyApi
inline fun <reified T : Parcelable> ParcelReader.readParcelableOrNull(): T? =
    readParcelableOrNull(clazz = T::class)
