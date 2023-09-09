@file:Suppress("MatchingDeclarationName")

package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE
import kotlin.reflect.KClass

/**
 * Allows serializing [Parcelable] classes when implementing custom parcelers using [CommonParceler].
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual class ParcelReader private constructor()

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readBoolean(): Boolean =
    error("Not yet supported")

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readInt(): Int =
    error("Not yet supported")

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readLong(): Long =
    error("Not yet supported")

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readFloat(): Float =
    error("Not yet supported")

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readDouble(): Double =
    error("Not yet supported")

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readStringOrNull(): String? =
    error("Not yet supported")

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun <T : Parcelable> ParcelReader.readParcelableOrNull(clazz: KClass<T>): T? =
    error("Not yet supported")

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@ExperimentalEssentyApi
actual fun ParcelReader.readByteArray(): ByteArray =
    error("Not yet supported")
