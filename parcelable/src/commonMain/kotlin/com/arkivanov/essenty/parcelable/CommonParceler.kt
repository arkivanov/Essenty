package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi

/**
 * Interface for [Parceler] implementations in common code (e.g. in `commonMain` source set).
 */
@ExperimentalEssentyApi
expect interface CommonParceler<T> : Parceler<T> {

    /**
     * Writes the [T] instance state to the provided [writer].
     */
    fun T.write(writer: ParcelWriter)

    /**
     * Creates a new instance of [T] and reads all its data from the provided [reader].
     */
    fun create(reader: ParcelReader): T
}
