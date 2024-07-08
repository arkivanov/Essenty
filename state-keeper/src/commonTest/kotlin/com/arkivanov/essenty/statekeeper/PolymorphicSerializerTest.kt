package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.test.Test
import kotlin.test.assertEquals

class PolymorphicSerializerTest {

    @Test
    fun serialize_and_deserialize() {
        val someListSerializer = ListSerializer(SomeSerializer)
        val originalSome = listOf(Some1(data = SerializableData()), Some2(data = SerializableData()))

        val newSome = originalSome.serialize(someListSerializer).deserialize(someListSerializer)

        assertEquals(originalSome, newSome)
    }

    private interface Some

    @Serializable
    private data class Some1(val data: SerializableData) : Some

    @Serializable
    private data class Some2(val data: SerializableData) : Some

    @OptIn(ExperimentalStateKeeperApi::class, ExperimentalSerializationApi::class)
    private object SomeSerializer : KSerializer<Some> by polymorphicSerializer(
        SerializersModule {
            polymorphic(Some::class) {
                subclass(Some1::class, Some1.serializer())
                subclass(Some2::class, Some2.serializer())
            }
        }
    )
}
