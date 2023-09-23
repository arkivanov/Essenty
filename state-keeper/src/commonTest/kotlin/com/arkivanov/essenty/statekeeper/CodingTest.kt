package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.test.Test
import kotlin.test.assertEquals

class CodingTest {

    @Test
    fun serializes_and_deserializes() {
        val some = getSome()

        val some2 = some.serialize(Some.serializer()).deserialize(Some.serializer())

        assertEquals(some, some2)
    }

    private fun getSome(): Some =
        Some(
            i1 = 1,
            i2 = 2,
            i3 = null,
            l1 = 1L,
            l2 = 2L,
            l3 = null,
            f1 = 1F,
            f2 = 2F,
            f3 = null,
            d1 = 1.0,
            d2 = 2.0,
            d3 = null,
            h1 = 1,
            h2 = 2,
            h3 = null,
            b1 = 1,
            b2 = 2,
            b3 = null,
            c1 = 'a',
            c2 = 'b',
            c3 = null,
            z1 = false,
            z2 = true,
            z3 = null,
            s1 = "str",
            s2 = "str",
            s3 = null,
            s4 = null,
            other1 = Other(a = 1),
            other2 = Other(a = 2),
            other3 = null,
            obj1 = Obj,
            obj2 = Obj,
            obj3 = null,
            enum1 = SomeEnum.A,
            enum2 = SomeEnum.B,
            enum3 = null,
            notParcelable11 = NotParcelable1(value = 1),
            notParcelable12 = NotParcelable1(value = 2),
            notParcelable13 = null,
            notParcelable21 = NotParcelable2(value = 1),
            notParcelable22 = NotParcelable2(value = 2),
            notParcelable23 = null,

            intList1 = listOf(1, 2),
            intList2 = listOf(3, 4),
            intList3 = null,
            intList4 = listOf(5, null),
            intList5 = listOf(6, null),
            intList6 = null,

            longList1 = listOf(1L, 2L),
            longList2 = listOf(3L, 4L),
            longList3 = null,
            longList4 = listOf(5L, null),
            longList5 = listOf(6L, null),
            longList6 = null,

            shortList1 = listOf(1.toShort(), 2.toShort()),
            shortList2 = listOf(3.toShort(), 4.toShort()),
            shortList3 = null,
            shortList4 = listOf(5.toShort(), null),
            shortList5 = listOf(6.toShort(), null),
            shortList6 = null,

            byteList1 = listOf(1.toByte(), 2.toByte()),
            byteList2 = listOf(3.toByte(), 4.toByte()),
            byteList3 = null,
            byteList4 = listOf(5.toByte(), null),
            byteList5 = listOf(6.toByte(), null),
            byteList6 = null,

            charList1 = listOf('a', 'b'),
            charList2 = listOf('c', 'd'),
            charList3 = null,
            charList4 = listOf('e', null),
            charList5 = listOf('f', null),
            charList6 = null,

            floatList1 = listOf(1F, 2F),
            floatList2 = listOf(3F, 4F),
            floatList3 = null,
            floatList4 = listOf(5F, null),
            floatList5 = listOf(6F, null),
            floatList6 = null,

            doubleList1 = listOf(1.0, 2.0),
            doubleList2 = listOf(3.0, 4.0),
            doubleList3 = null,
            doubleList4 = listOf(5.0, null),
            doubleList5 = listOf(6.0, null),
            doubleList6 = null,

            booleanList1 = listOf(false, true),
            booleanList2 = listOf(true, false),
            booleanList3 = null,
            booleanList4 = listOf(false, null),
            booleanList5 = listOf(true, null),
            booleanList6 = null,

            stringList1 = listOf("a", "b"),
            stringList2 = listOf("c", "d"),
            stringList3 = null,
            stringList4 = listOf("e", null),
            stringList5 = listOf("f", null),
            stringList6 = null,

            parcelableList1 = listOf(Other(a = 1), Other(a = 2)),
            parcelableList2 = listOf(Other(a = 3), Other(a = 4)),
            parcelableList3 = null,
            parcelableList4 = listOf(Other(a = 5), null),
            parcelableList5 = listOf(Other(a = 6), null),
            parcelableList6 = null,

            intSet1 = setOf(1, 2),
            intSet2 = setOf(3, 4),
            intSet3 = null,
            intSet4 = setOf(5, null),
            intSet5 = setOf(6, null),
            intSet6 = null,

            longSet1 = setOf(1L, 2L),
            longSet2 = setOf(3L, 4L),
            longSet3 = null,
            longSet4 = setOf(5L, null),
            longSet5 = setOf(6L, null),
            longSet6 = null,

            shortSet1 = setOf(1.toShort(), 2.toShort()),
            shortSet2 = setOf(3.toShort(), 4.toShort()),
            shortSet3 = null,
            shortSet4 = setOf(5.toShort(), null),
            shortSet5 = setOf(6.toShort(), null),
            shortSet6 = null,

            byteSet1 = setOf(1.toByte(), 2.toByte()),
            byteSet2 = setOf(3.toByte(), 4.toByte()),
            byteSet3 = null,
            byteSet4 = setOf(5.toByte(), null),
            byteSet5 = setOf(6.toByte(), null),
            byteSet6 = null,

            charSet1 = setOf('a', 'b'),
            charSet2 = setOf('c', 'd'),
            charSet3 = null,
            charSet4 = setOf('e', null),
            charSet5 = setOf('f', null),
            charSet6 = null,

            floatSet1 = setOf(1F, 2F),
            floatSet2 = setOf(3F, 4F),
            floatSet3 = null,
            floatSet4 = setOf(5F, null),
            floatSet5 = setOf(6F, null),
            floatSet6 = null,

            doubleSet1 = setOf(1.0, 2.0),
            doubleSet2 = setOf(3.0, 4.0),
            doubleSet3 = null,
            doubleSet4 = setOf(5.0, null),
            doubleSet5 = setOf(6.0, null),
            doubleSet6 = null,

            booleanSet1 = setOf(false, true),
            booleanSet2 = setOf(true, false),
            booleanSet3 = null,
            booleanSet4 = setOf(false, null),
            booleanSet5 = setOf(true, null),
            booleanSet6 = null,

            stringSet1 = setOf("a", "b"),
            stringSet2 = setOf("c", "d"),
            stringSet3 = null,
            stringSet4 = setOf("e", null),
            stringSet5 = setOf("f", null),
            stringSet6 = null,

            parcelableSet1 = setOf(Other(a = 1), Other(a = 2)),
            parcelableSet2 = setOf(Other(a = 3), Other(a = 4)),
            parcelableSet3 = null,
            parcelableSet4 = setOf(Other(a = 5), null),
            parcelableSet5 = setOf(Other(a = 6), null),
            parcelableSet6 = null,

            intMap1 = mapOf(1 to 11, 2 to 22),
            intMap2 = mapOf(3 to 33, 4 to 44),
            intMap3 = null,
            intMap4 = mapOf(5 to 55, null to null),
            intMap5 = mapOf(6 to 66, null to null),
            intMap6 = null,

            longMap1 = mapOf(1L to 11L, 2L to 22L),
            longMap2 = mapOf(3L to 33L, 4L to 44L),
            longMap3 = null,
            longMap4 = mapOf(5L to 55L, null to null),
            longMap5 = mapOf(6L to 66L, null to null),
            longMap6 = null,

            shortMap1 = mapOf(1.toShort() to 11.toShort(), 2.toShort() to 22.toShort()),
            shortMap2 = mapOf(3.toShort() to 33.toShort(), 4.toShort() to 44.toShort()),
            shortMap3 = null,
            shortMap4 = mapOf(5.toShort() to 55.toShort(), null to null),
            shortMap5 = mapOf(6.toShort() to 66.toShort(), null to null),
            shortMap6 = null,

            byteMap1 = mapOf(1.toByte() to 11.toByte(), 2.toByte() to 22.toByte()),
            byteMap2 = mapOf(3.toByte() to 33.toByte(), 4.toByte() to 44.toByte()),
            byteMap3 = null,
            byteMap4 = mapOf(5.toByte() to 55.toByte(), null to null),
            byteMap5 = mapOf(6.toByte() to 66.toByte(), null to null),
            byteMap6 = null,

            charMap1 = mapOf('a' to 'A', 'b' to 'B'),
            charMap2 = mapOf('c' to 'C', 'd' to 'D'),
            charMap3 = null,
            charMap4 = mapOf('e' to 'E', null to null),
            charMap5 = mapOf('f' to 'F', null to null),
            charMap6 = null,

            floatMap1 = mapOf(1F to 11F, 2F to 22F),
            floatMap2 = mapOf(3F to 33F, 4F to 44F),
            floatMap3 = null,
            floatMap4 = mapOf(5F to 55F, null to null),
            floatMap5 = mapOf(6F to 66F, null to null),
            floatMap6 = null,

            doubleMap1 = mapOf(1.0 to 11.0, 2.0 to 22.0),
            doubleMap2 = mapOf(3.0 to 33.0, 4.0 to 44.0),
            doubleMap3 = null,
            doubleMap4 = mapOf(5.0 to 55.0, null to null),
            doubleMap5 = mapOf(6.0 to 66.0, null to null),
            doubleMap6 = null,

//            booleanMap1 = mapOf(false to true, true to false),
//            booleanMap2 = mapOf(true to false, false to true),
            booleanMap3 = null,
//            booleanMap4 = mapOf(false to true, null to null),
//            booleanMap5 = mapOf(true to false, null to null),
            booleanMap6 = null,

            stringMap1 = mapOf("a" to "A", "b" to "B"),
            stringMap2 = mapOf("c" to "C", "d" to "D"),
            stringMap3 = null,
            stringMap4 = mapOf("e" to "E", null to null),
            stringMap5 = mapOf("f" to "F", null to null),
            stringMap6 = null,

            parcelableMap1 = mapOf(Other(a = 1) to Other(a = 11), Other(a = 2) to Other(a = 22)),
            parcelableMap2 = mapOf(Other(a = 3) to Other(a = 33), Other(a = 4) to Other(a = 44)),
            parcelableMap3 = null,
            parcelableMap4 = mapOf(Other(a = 5) to Other(a = 55), null to null),
            parcelableMap5 = mapOf(Other(a = 6) to Other(a = 66), null to null),
            parcelableMap6 = null,
        )

    @Serializable
    private data class Other(
        val a: Int
    ) : SomeClass()

    @Serializable
    private data object Obj

    private enum class SomeEnum {
        A, B
    }

    @Serializable
    private data class Some(
        val i1: Int,
        val i2: Int?,
        val i3: Int?,
        val l1: Long,
        val l2: Long?,
        val l3: Long?,
        val f1: Float,
        val f2: Float?,
        val f3: Float?,
        val d1: Double,
        val d2: Double?,
        val d3: Double?,
        val h1: Short,
        val h2: Short?,
        val h3: Short?,
        val b1: Byte,
        val b2: Byte?,
        val b3: Byte?,
        val c1: Char,
        val c2: Char?,
        val c3: Char?,
        val z1: Boolean,
        val z2: Boolean?,
        val z3: Boolean?,
        val s1: String,
        val s2: String?,
        val s3: String?,
        val s4: String? = "",
        val other1: Other,
        val other2: Other?,
        val other3: Other?,
        val obj1: Obj,
        val obj2: Obj?,
        val obj3: Obj?,
        val enum1: SomeEnum,
        val enum2: SomeEnum?,
        val enum3: SomeEnum?,
        @Serializable(with = NotParcelable1Serializer::class) val notParcelable11: NotParcelable1,
        @Serializable(with = NotParcelable1Serializer::class) val notParcelable12: NotParcelable1?,
        @Serializable(with = NotParcelable1Serializer::class) val notParcelable13: NotParcelable1?,
        @Serializable(with = NotParcelable2Serializer::class) val notParcelable21: NotParcelable2,
        @Serializable(with = NotParcelable2Serializer::class) val notParcelable22: NotParcelable2?,
        @Serializable(with = NotParcelable2Serializer::class) val notParcelable23: NotParcelable2?,

        val intList1: List<Int>,
        val intList2: List<Int>?,
        val intList3: List<Int>?,
        val intList4: List<Int?>,
        val intList5: List<Int?>?,
        val intList6: List<Int?>?,

        val longList1: List<Long>,
        val longList2: List<Long>?,
        val longList3: List<Long>?,
        val longList4: List<Long?>,
        val longList5: List<Long?>?,
        val longList6: List<Long?>?,

        val shortList1: List<Short>,
        val shortList2: List<Short>?,
        val shortList3: List<Short>?,
        val shortList4: List<Short?>,
        val shortList5: List<Short?>?,
        val shortList6: List<Short?>?,

        val byteList1: List<Byte>,
        val byteList2: List<Byte>?,
        val byteList3: List<Byte>?,
        val byteList4: List<Byte?>,
        val byteList5: List<Byte?>?,
        val byteList6: List<Byte?>?,

        val charList1: List<Char>,
        val charList2: List<Char>?,
        val charList3: List<Char>?,
        val charList4: List<Char?>,
        val charList5: List<Char?>?,
        val charList6: List<Char?>?,

        val floatList1: List<Float>,
        val floatList2: List<Float>?,
        val floatList3: List<Float>?,
        val floatList4: List<Float?>,
        val floatList5: List<Float?>?,
        val floatList6: List<Float?>?,

        val doubleList1: List<Double>,
        val doubleList2: List<Double>?,
        val doubleList3: List<Double>?,
        val doubleList4: List<Double?>,
        val doubleList5: List<Double?>?,
        val doubleList6: List<Double?>?,

        val booleanList1: List<Boolean>,
        val booleanList2: List<Boolean>?,
        val booleanList3: List<Boolean>?,
        val booleanList4: List<Boolean?>,
        val booleanList5: List<Boolean?>?,
        val booleanList6: List<Boolean?>?,

        val stringList1: List<String>,
        val stringList2: List<String>?,
        val stringList3: List<String>?,
        val stringList4: List<String?>,
        val stringList5: List<String?>?,
        val stringList6: List<String?>?,

        val parcelableList1: List<Other>,
        val parcelableList2: List<Other>?,
        val parcelableList3: List<Other>?,
        val parcelableList4: List<Other?>,
        val parcelableList5: List<Other?>?,
        val parcelableList6: List<Other?>?,

        val intSet1: Set<Int>,
        val intSet2: Set<Int>?,
        val intSet3: Set<Int>?,
        val intSet4: Set<Int?>,
        val intSet5: Set<Int?>?,
        val intSet6: Set<Int?>?,

        val longSet1: Set<Long>,
        val longSet2: Set<Long>?,
        val longSet3: Set<Long>?,
        val longSet4: Set<Long?>,
        val longSet5: Set<Long?>?,
        val longSet6: Set<Long?>?,

        val shortSet1: Set<Short>,
        val shortSet2: Set<Short>?,
        val shortSet3: Set<Short>?,
        val shortSet4: Set<Short?>,
        val shortSet5: Set<Short?>?,
        val shortSet6: Set<Short?>?,

        val byteSet1: Set<Byte>,
        val byteSet2: Set<Byte>?,
        val byteSet3: Set<Byte>?,
        val byteSet4: Set<Byte?>,
        val byteSet5: Set<Byte?>?,
        val byteSet6: Set<Byte?>?,

        val charSet1: Set<Char>,
        val charSet2: Set<Char>?,
        val charSet3: Set<Char>?,
        val charSet4: Set<Char?>,
        val charSet5: Set<Char?>?,
        val charSet6: Set<Char?>?,

        val floatSet1: Set<Float>,
        val floatSet2: Set<Float>?,
        val floatSet3: Set<Float>?,
        val floatSet4: Set<Float?>,
        val floatSet5: Set<Float?>?,
        val floatSet6: Set<Float?>?,

        val doubleSet1: Set<Double>,
        val doubleSet2: Set<Double>?,
        val doubleSet3: Set<Double>?,
        val doubleSet4: Set<Double?>,
        val doubleSet5: Set<Double?>?,
        val doubleSet6: Set<Double?>?,

        val booleanSet1: Set<Boolean>,
        val booleanSet2: Set<Boolean>?,
        val booleanSet3: Set<Boolean>?,
        val booleanSet4: Set<Boolean?>,
        val booleanSet5: Set<Boolean?>?,
        val booleanSet6: Set<Boolean?>?,

        val stringSet1: Set<String>,
        val stringSet2: Set<String>?,
        val stringSet3: Set<String>?,
        val stringSet4: Set<String?>,
        val stringSet5: Set<String?>?,
        val stringSet6: Set<String?>?,

        val parcelableSet1: Set<Other>,
        val parcelableSet2: Set<Other>?,
        val parcelableSet3: Set<Other>?,
        val parcelableSet4: Set<Other?>,
        val parcelableSet5: Set<Other?>?,
        val parcelableSet6: Set<Other?>?,

        val intMap1: Map<Int, Int>,
        val intMap2: Map<Int, Int>?,
        val intMap3: Map<Int, Int>?,
        val intMap4: Map<Int?, Int?>,
        val intMap5: Map<Int?, Int?>?,
        val intMap6: Map<Int?, Int?>?,

        val longMap1: Map<Long, Long>,
        val longMap2: Map<Long, Long>?,
        val longMap3: Map<Long, Long>?,
        val longMap4: Map<Long?, Long?>,
        val longMap5: Map<Long?, Long?>?,
        val longMap6: Map<Long?, Long?>?,

        val shortMap1: Map<Short, Short>,
        val shortMap2: Map<Short, Short>?,
        val shortMap3: Map<Short, Short>?,
        val shortMap4: Map<Short?, Short?>,
        val shortMap5: Map<Short?, Short?>?,
        val shortMap6: Map<Short?, Short?>?,

        val byteMap1: Map<Byte, Byte>,
        val byteMap2: Map<Byte, Byte>?,
        val byteMap3: Map<Byte, Byte>?,
        val byteMap4: Map<Byte?, Byte?>,
        val byteMap5: Map<Byte?, Byte?>?,
        val byteMap6: Map<Byte?, Byte?>?,

        val charMap1: Map<Char, Char>,
        val charMap2: Map<Char, Char>?,
        val charMap3: Map<Char, Char>?,
        val charMap4: Map<Char?, Char?>,
        val charMap5: Map<Char?, Char?>?,
        val charMap6: Map<Char?, Char?>?,

        val floatMap1: Map<Float, Float>,
        val floatMap2: Map<Float, Float>?,
        val floatMap3: Map<Float, Float>?,
        val floatMap4: Map<Float?, Float?>,
        val floatMap5: Map<Float?, Float?>?,
        val floatMap6: Map<Float?, Float?>?,

        val doubleMap1: Map<Double, Double>,
        val doubleMap2: Map<Double, Double>?,
        val doubleMap3: Map<Double, Double>?,
        val doubleMap4: Map<Double?, Double?>,
        val doubleMap5: Map<Double?, Double?>?,
        val doubleMap6: Map<Double?, Double?>?,

//        val booleanMap1: Map<Boolean, Boolean>,
//        val booleanMap2: Map<Boolean, Boolean>?,
        val booleanMap3: Map<Boolean, Boolean>?,
//        val booleanMap4: Map<Boolean?, Boolean?>,
//        val booleanMap5: Map<Boolean?, Boolean?>?,
        val booleanMap6: Map<Boolean?, Boolean?>?,

        val stringMap1: Map<String, String>,
        val stringMap2: Map<String, String>?,
        val stringMap3: Map<String, String>?,
        val stringMap4: Map<String?, String?>,
        val stringMap5: Map<String?, String?>?,
        val stringMap6: Map<String?, String?>?,

        val parcelableMap1: Map<Other, Other>,
        val parcelableMap2: Map<Other, Other>?,
        val parcelableMap3: Map<Other, Other>?,
        val parcelableMap4: Map<Other?, Other?>,
        val parcelableMap5: Map<Other?, Other?>?,
        val parcelableMap6: Map<Other?, Other?>?,
    ) : SomeInterface

    private interface SomeInterface

    private abstract class SomeClass

    private data class NotParcelable1(
        val value: Int,
    )

    private object NotParcelable1Serializer : KSerializer<NotParcelable1> {
        override val descriptor: SerialDescriptor = Int.serializer().descriptor

        override fun serialize(encoder: Encoder, value: NotParcelable1) {
            encoder.encodeInt(value.value)
        }

        override fun deserialize(decoder: Decoder): NotParcelable1 =
            NotParcelable1(value = decoder.decodeInt())
    }

    private data class NotParcelable2(
        val value: Int,
    )

    private object NotParcelable2Serializer : KSerializer<NotParcelable2> {
        override val descriptor: SerialDescriptor = Int.serializer().descriptor

        override fun serialize(encoder: Encoder, value: NotParcelable2) {
            encoder.encodeInt(value.value)
        }

        override fun deserialize(decoder: Decoder): NotParcelable2 =
            NotParcelable2(value = decoder.decodeInt())
    }
}
