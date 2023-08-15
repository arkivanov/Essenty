package com.arkivanov.essenty.parcelable

import com.arkivanov.parcelize.darwin.decodeParcelable
import com.arkivanov.parcelize.darwin.encodeParcelable
import platform.Foundation.NSKeyedArchiver
import platform.Foundation.NSKeyedUnarchiver

class ParcelizeTest : AbstractParcelizeTest() {

    override fun serializeAndDeserialize(data: Data): Data {
        val archiver = NSKeyedArchiver(requiringSecureCoding = true)
        archiver.encodeParcelable(value = data, key = "data")
        val unarchiver = NSKeyedUnarchiver(forReadingWithData = archiver.encodedData)
        unarchiver.requiresSecureCoding = true

        return unarchiver.decodeParcelable(key = "data")
    }
}
