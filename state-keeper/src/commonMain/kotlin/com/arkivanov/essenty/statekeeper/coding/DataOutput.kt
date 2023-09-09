package com.arkivanov.essenty.statekeeper.coding

internal interface DataOutput {

    fun writeByte(byte: Byte)
}

internal fun DataOutput.writeBoolean(value: Boolean) {
    writeByte(if (value) 1 else 0)
}

internal fun DataOutput.writeShort(value: Short) {
    writeByte(value.toByte())
    writeByte((value.toInt() shr 8).toByte())
}

internal fun DataOutput.writeInt(value: Int) {
    writeByte(value.toByte())
    writeByte((value shr 8).toByte())
    writeByte((value shr 16).toByte())
    writeByte((value shr 24).toByte())
}

internal fun DataOutput.writeLong(value: Long) {
    writeInt(value.toInt())
    writeInt((value shr 32).toInt())
}

internal expect fun DataOutput.writeFloat(value: Float)

internal fun DataOutput.writeDouble(value: Double) {
    writeLong(value.toBits())
}

internal fun DataOutput.writeChar(value: Char) {
    writeShort(value.code.toShort())
}

internal fun DataOutput.writeString(value: String) {
    val bytes = value.encodeToByteArray()
    writeInt(bytes.size)
    bytes.forEach(::writeByte)
}
