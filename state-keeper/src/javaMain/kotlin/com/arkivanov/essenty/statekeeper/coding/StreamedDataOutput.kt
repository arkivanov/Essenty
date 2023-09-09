package com.arkivanov.essenty.statekeeper.coding

import java.io.OutputStream

internal class StreamedDataOutput(
    private val output: OutputStream,
) : DataOutput {

    override fun writeByte(byte: Byte) {
        output.write(byte.toInt())
    }
}
