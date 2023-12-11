package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.statekeeper.base64.ByteArrayAsBase64StringSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
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
        private val serializer = ByteArrayAsBase64StringSerializer.nullable
        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: SerializableContainer) {
            serializer.serialize(encoder, (value.holder?.serialize() ?: value.data))
        }

        private fun <T : Any> Holder<T>.serialize(): ByteArray? =
            value?.serialize(strategy)

        override fun deserialize(decoder: Decoder): SerializableContainer =
            SerializableContainer(data = serializer.deserialize(decoder))
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
