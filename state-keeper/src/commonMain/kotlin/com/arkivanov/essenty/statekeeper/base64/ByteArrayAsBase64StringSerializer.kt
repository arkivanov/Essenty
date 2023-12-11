package com.arkivanov.essenty.statekeeper.base64

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer that encodes and decodes [ByteArray] using [Base64](https://en.wikipedia.org/wiki/Base64) encodings.
 * This is usually makes sense with text formats like JSON.
 */
internal object ByteArrayAsBase64StringSerializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
            "kotlinx.serialization.ByteArrayAsBase64StringSerializer",
            PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): ByteArray {
        return decode(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(encode(value))
    }
}
