package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Represents a lazy [Serializable][kotlinx.serialization.Serializable] container for a `Serializable` object.
 */
@Serializable(with = SerializableContainer.Serializer::class)
class SerializableContainer private constructor(
    private var data: ByteArray?,
) {
    constructor() : this(data = null)

    private var holder: Holder<*>? = null

    /**
     * Deserializes and returns a previously stored [Serializable][kotlinx.serialization.Serializable] object.
     *
     * @param strategy a [DeserializationStrategy] for deserializing the object.
     */
    fun <T : Any> consume(strategy: DeserializationStrategy<T>): T? {
        val consumedValue: Any? = holder?.value ?: data?.deserialize(strategy)
        holder = null
        data = null

        @Suppress("UNCHECKED_CAST")
        return consumedValue as T?
    }

    /**
     * Stores a [Serializable][kotlinx.serialization.Serializable] object, replacing any previously stored object.
     *
     * @param value an object to be stored and serialized later when needed.
     * @param strategy a [SerializationStrategy] for serializing the value.
     */
    fun <T : Any> set(value: T?, strategy: SerializationStrategy<T>) {
        holder = Holder(value = value, strategy = strategy)
        data = null
    }

    /**
     * Clears any previously stored object.
     */
    fun clear() {
        holder = null
        data = null
    }

    private class Holder<T : Any>(
        val value: T?,
        val strategy: SerializationStrategy<T>,
    )

    internal object Serializer : KSerializer<SerializableContainer> {
        private val byteArraySerializer = ByteArraySerializer()

        override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor(serialName = "SerializableContainer") {
                element<Boolean>(elementName = "exists")
                element(elementName = "data", descriptor = byteArraySerializer.descriptor, isOptional = true)
            }

        override fun serialize(encoder: Encoder, value: SerializableContainer) {
            val data = value.holder?.serialize()
            encoder.encodeBoolean(data != null)
            if (data != null) {
                encoder.encodeSerializableValue(serializer = byteArraySerializer, value = data)
            }
        }

        private fun <T : Any> Holder<T>.serialize(): ByteArray? =
            value?.serialize(strategy)

        override fun deserialize(decoder: Decoder): SerializableContainer =
            SerializableContainer(data = decoder.takeIf(Decoder::decodeBoolean)?.decodeSerializableValue(byteArraySerializer))
    }
}

/**
 * Creates a new [SerializableContainer] and sets the provided [value] with the provided [strategy].
 */
fun <T : Any> SerializableContainer(
    value: T?,
    strategy: SerializationStrategy<T>
): SerializableContainer =
    SerializableContainer().apply {
        set(value = value, strategy = strategy)
    }

/**
 * A convenience method for [SerializableContainer.consume]. Throws [IllegalStateException]
 * if the [SerializableContainer] is empty.
 */
fun <T : Any> SerializableContainer.consumeRequired(strategy: DeserializationStrategy<T>): T =
    checkNotNull(consume(strategy))
