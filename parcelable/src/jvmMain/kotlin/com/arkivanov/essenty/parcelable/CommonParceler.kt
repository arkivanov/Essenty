package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi

/**
 * Interface for [Parceler] implementations in common code (e.g. in `commonMain` source set).
 */
@ExperimentalEssentyApi
actual interface CommonParceler<T> : Parceler<T> {

    /**
     * Writes the [T] instance state to the provided [writer].
     */
    actual fun T.write(writer: ParcelWriter)

    /**
     * Creates a new instance of [T] and reads all its data from the provided [reader].
     */
    actual fun create(reader: ParcelReader): T
}
