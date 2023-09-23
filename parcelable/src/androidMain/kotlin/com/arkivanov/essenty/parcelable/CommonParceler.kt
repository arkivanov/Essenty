package com.arkivanov.essenty.parcelable

import android.os.Parcel
import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

/**
 * Interface for [Parceler] implementations in common code (e.g. in `commonMain` source set).
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
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

    override fun create(parcel: Parcel): T =
        create(ParcelReader(parcel))

    override fun T.write(parcel: Parcel, flags: Int) {
        write(ParcelWriter(parcel))
    }
}
