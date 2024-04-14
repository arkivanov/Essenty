package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import android.os.Parcel

internal fun Bundle.parcelize(): ByteArray {
    val parcel = Parcel.obtain()
    parcel.writeBundle(this)
    return parcel.marshall()
}

internal fun ByteArray.deparcelize(): Bundle {
    val parcel = Parcel.obtain()
    parcel.unmarshall(this, 0, size)
    parcel.setDataPosition(0)

    return requireNotNull(parcel.readBundle())
}
