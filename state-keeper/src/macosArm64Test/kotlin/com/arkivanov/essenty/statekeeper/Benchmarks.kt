package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.parcelize.darwin.decodeParcelable
import com.arkivanov.parcelize.darwin.encodeParcelable
import platform.Foundation.NSData
import platform.Foundation.NSKeyedArchiver
import platform.Foundation.NSKeyedUnarchiver
import kotlin.reflect.KClass

class Benchmarks : AbstractBenchmarks<NSData>() {

    override fun Parcelable.serialize(): NSData {
        val arch = NSKeyedArchiver(requiringSecureCoding = true)
        arch.encodeParcelable(value = this, key = "key")
        return arch.encodedData
    }

    override fun <T : Parcelable> NSData.deserialize(clazz: KClass<T>): T {
        val unarch = NSKeyedUnarchiver(forReadingWithData = this)
        unarch.requiresSecureCoding = true

        return unarch.decodeParcelable("key")
    }

    override fun NSData.getSizeBytes(): Int =
        length.toInt()
}
