package com.arkivanov.essenty.statekeeper.coding

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
internal class DefaultDecoder(
    private val input: DataInput,
) : AbstractDecoder() {

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun decodeNotNullMark(): Boolean =
        input.readBoolean()

    override fun decodeBoolean(): Boolean =
        input.readBoolean()

    override fun decodeByte(): Byte =
        input.readByte()

    override fun decodeShort(): Short =
        input.readShort()

    override fun decodeInt(): Int =
        input.readInt()

    override fun decodeLong(): Long =
        input.readLong()

    override fun decodeFloat(): Float =
        input.readFloat()

    override fun decodeDouble(): Double =
        input.readDouble()

    override fun decodeChar(): Char =
        input.readChar()

    override fun decodeString(): String =
        input.readString()

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        input.readInt()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        val key = input.readString()
        return if (key.isNotEmpty()) descriptor.getElementIndex(name = key) else DECODE_DONE
    }
}
