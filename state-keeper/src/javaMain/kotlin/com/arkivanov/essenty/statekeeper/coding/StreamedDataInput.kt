package com.arkivanov.essenty.statekeeper.coding

import java.io.InputStream

internal class StreamedDataInput(
    private val input: InputStream,
) : DataInput {

    override fun readByte(): Byte =
        input.read().also { check(it >= 0) }.toByte()
}
