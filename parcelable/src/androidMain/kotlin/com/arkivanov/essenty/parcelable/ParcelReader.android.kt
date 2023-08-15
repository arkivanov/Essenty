@file:Suppress("MatchingDeclarationName")

package com.arkivanov.essenty.parcelable

import android.os.Parcel
import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import kotlin.reflect.KClass

/**
 * Allows serializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 * Serialization is performed via [android.os.Parcel].
 */
@ExperimentalEssentyApi
actual class ParcelReader internal constructor(internal val parcel: Parcel)

@ExperimentalEssentyApi
actual fun ParcelReader.readBoolean(): Boolean =
    parcel.readInt() != 0

@ExperimentalEssentyApi
actual fun ParcelReader.readInt(): Int =
    parcel.readInt()

@ExperimentalEssentyApi
actual fun ParcelReader.readLong(): Long =
    parcel.readLong()

@ExperimentalEssentyApi
actual fun ParcelReader.readFloat(): Float =
    parcel.readFloat()

@ExperimentalEssentyApi
actual fun ParcelReader.readDouble(): Double =
    parcel.readDouble()

@ExperimentalEssentyApi
actual fun ParcelReader.readStringOrNull(): String? =
    parcel.readString()

@Suppress("DEPRECATION")
@ExperimentalEssentyApi
actual fun <T : Parcelable> ParcelReader.readParcelableOrNull(clazz: KClass<T>): T? =
    parcel.readParcelable(clazz.java.classLoader)
