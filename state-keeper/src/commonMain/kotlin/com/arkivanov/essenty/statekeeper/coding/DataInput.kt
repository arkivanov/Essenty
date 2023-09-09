package com.arkivanov.essenty.statekeeper.coding

internal interface DataInput {

    fun readByte(): Byte
}

internal fun DataInput.readBoolean(): Boolean =
    readByte() != 0.toByte()

internal fun DataInput.readShort(): Short {
    val a = readByte().toInt()
    val b = readByte().toInt()

    return ((a and 0xFF) or ((b and 0xFF) shl 8)).toShort()
}

internal fun DataInput.readInt(): Int {
    val a = readByte().toInt()
    val b = readByte().toInt()
    val c = readByte().toInt()
    val d = readByte().toInt()

    return (a and 0xFF) or ((b and 0xFF) shl 8) or ((c and 0xFF) shl 16) or ((d and 0xFF) shl 24)
}

internal fun DataInput.readLong(): Long {
    val a = readInt().toLong()
    val b = readInt().toLong()

    return (a and 0xFFFFFFFF) or ((b and 0xFFFFFFFF) shl 32)
}

internal expect fun DataInput.readFloat(): Float

internal fun DataInput.readDouble(): Double =
    Double.fromBits(readLong())

internal fun DataInput.readChar(): Char =
    readShort().toUShort().toInt().toChar()

internal fun DataInput.readString(): String =
    ByteArray(readInt()) { readByte() }.decodeToString()
