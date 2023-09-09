package com.arkivanov.essenty.statekeeper.coding

internal class ByteArrayDataInput(
    private var bytes: ByteArray,
) : DataInput {

    private var index = 0

    override fun readByte(): Byte =
        bytes[index++]
}
