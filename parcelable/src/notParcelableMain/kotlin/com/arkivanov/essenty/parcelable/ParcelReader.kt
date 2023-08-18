package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import kotlin.reflect.KClass

/**
 * Allows serializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 */
@ExperimentalEssentyApi
actual class ParcelReader private constructor()

@ExperimentalEssentyApi
actual fun ParcelReader.readBoolean(): Boolean =
    error("Not yet supported")

@ExperimentalEssentyApi
actual fun ParcelReader.readInt(): Int =
    error("Not yet supported")

@ExperimentalEssentyApi
actual fun ParcelReader.readLong(): Long =
    error("Not yet supported")

@ExperimentalEssentyApi
actual fun ParcelReader.readFloat(): Float =
    error("Not yet supported")

@ExperimentalEssentyApi
actual fun ParcelReader.readDouble(): Double =
    error("Not yet supported")

@ExperimentalEssentyApi
actual fun ParcelReader.readStringOrNull(): String? =
    error("Not yet supported")

@ExperimentalEssentyApi
actual fun <T : Parcelable> ParcelReader.readParcelableOrNull(clazz: KClass<T>): T? =
    error("Not yet supported")
