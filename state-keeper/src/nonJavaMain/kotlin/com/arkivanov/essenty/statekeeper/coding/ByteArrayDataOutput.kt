package com.arkivanov.essenty.statekeeper.coding

internal class ByteArrayDataOutput : DataOutput {

    private var buffer = ByteArray(size = 16)
    private var size = 0

    override fun writeByte(byte: Byte) {
        if (buffer.size - size < 1) {
            buffer = buffer.copyOf(newSize = buffer.size * 2)
        }

        buffer[size++] = byte
    }

    fun getBytes(): ByteArray =
        buffer.copyOf(newSize = size)
}
