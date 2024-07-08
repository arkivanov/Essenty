package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass

/**
 * Creates a polymorphic [KSerializer] for the specified class of type [T] using the specified [module].
 */
@ExperimentalStateKeeperApi
@ExperimentalSerializationApi
inline fun <reified T : Any> polymorphicSerializer(module: SerializersModule): KSerializer<T> =
    polymorphicSerializer(baseClass = T::class, module = module)

/**
 * Creates a polymorphic [KSerializer] for the specified [baseClass] class using the specified [module].
 */
@ExperimentalStateKeeperApi
@ExperimentalSerializationApi
fun <T : Any> polymorphicSerializer(baseClass: KClass<T>, module: SerializersModule): KSerializer<T> =
    PolymorphicSerializer(baseClass = baseClass, module = module)

@ExperimentalSerializationApi
private class PolymorphicSerializer<T : Any>(
    private val baseClass: KClass<T>,
    private val module: SerializersModule,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("PolymorphicSerializer") {
            element<String>("type")
            element("value", ContextualSerialDescriptor)
        }

    override fun serialize(encoder: Encoder, value: T) {
        val serializer = requireNotNull(module.getPolymorphic(baseClass, value))
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, serializer.descriptor.serialName)
            encodeSerializableElement(descriptor, 1, serializer, value)
        }
    }

    override fun deserialize(decoder: Decoder): T =
        decoder.decodeStructure(descriptor) {
            var className: String? = null
            var value: T? = null

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> className = decodeStringElement(descriptor, index)

                    1 -> {
                        val actualClassName = requireNotNull(className)
                        val serializer = requireNotNull(module.getPolymorphic(baseClass, actualClassName))
                        value = decodeSerializableElement(descriptor, 1, serializer)
                    }

                    CompositeDecoder.DECODE_DONE -> break

                    else -> error("Unsupported index: $index")
                }
            }

            requireNotNull(value)
        }

    private object ContextualSerialDescriptor : SerialDescriptor {
        override val elementsCount: Int = 0
        override val kind: SerialKind = SerialKind.CONTEXTUAL
        override val serialName: String = "Value"

        override fun getElementAnnotations(index: Int): List<Annotation> = elementNotFoundError(index)
        override fun getElementDescriptor(index: Int): SerialDescriptor = elementNotFoundError(index)
        override fun getElementIndex(name: String): Int = CompositeDecoder.UNKNOWN_NAME
        override fun getElementName(index: Int): String = elementNotFoundError(index)
        override fun isElementOptional(index: Int): Boolean = elementNotFoundError(index)

        private fun elementNotFoundError(index: Int): Nothing {
            throw IndexOutOfBoundsException("Element at index $index not found")
        }
    }
}
