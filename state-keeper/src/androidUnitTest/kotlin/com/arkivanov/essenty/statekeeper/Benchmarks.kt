package com.arkivanov.essenty.statekeeper

import android.os.Parcel
import com.arkivanov.essenty.parcelable.Parcelable
import kotlin.reflect.KClass

//@RunWith(RobolectricTestRunner::class)
class Benchmarks : AbstractBenchmarks<ByteArray>() {

    override fun Parcelable.serialize(): ByteArray {
        val parcel = Parcel.obtain()
        parcel.writeParcelable(this, 0)
        return parcel.marshall()
    }

    override fun <T : Parcelable> ByteArray.deserialize(clazz: KClass<T>): T {
        val parcel = Parcel.obtain()
        parcel.unmarshall(this, 0, size)
        parcel.setDataPosition(0)

        @Suppress("DEPRECATION")
        return requireNotNull(parcel.readParcelable(clazz.java.classLoader))
    }

    override fun ByteArray.getSizeBytes(): Int =
        size
}
