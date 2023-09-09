package com.arkivanov.essenty.statekeeper.coding

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
internal class DefaultEncoder(
    private val output: DataOutput,
) : AbstractEncoder() {

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        output.writeString(descriptor.getElementName(index))
        return true
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        output.writeString("")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun encodeNotNullMark() {
        output.writeBoolean(true)
    }

    @ExperimentalSerializationApi
    override fun encodeNull() {
        output.writeBoolean(false)
    }

    override fun encodeBoolean(value: Boolean) {
        output.writeBoolean(value)
    }

    override fun encodeByte(value: Byte) {
        output.writeByte(value)
    }

    override fun encodeShort(value: Short) {
        output.writeShort(value)
    }

    override fun encodeChar(value: Char) {
        output.writeChar(value)
    }

    override fun encodeInt(value: Int) {
        output.writeInt(value)
    }

    override fun encodeLong(value: Long) {
        output.writeLong(value)
    }


    override fun encodeFloat(value: Float) {
        output.writeFloat(value)
    }

    override fun encodeDouble(value: Double) {
        output.writeDouble(value)
    }

    override fun encodeString(value: String) {
        output.writeString(value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        output.writeInt(index)
    }
}

