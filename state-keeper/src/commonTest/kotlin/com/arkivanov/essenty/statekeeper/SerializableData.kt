package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Suppress("DataClassPrivateConstructor")
@ConsistentCopyVisibility
@Serializable
data class SerializableData private constructor(
    private val i1: Int,
    private val i2: Int?,
    private val i3: Int?,
    private val l1: Long,
    private val l2: Long?,
    private val l3: Long?,
    private val f1: Float,
    private val f2: Float?,
    private val f3: Float?,
    private val d1: Double,
    private val d2: Double?,
    private val d3: Double?,
    private val h1: Short,
    private val h2: Short?,
    private val h3: Short?,
    private val b1: Byte,
    private val b2: Byte?,
    private val b3: Byte?,
    private val c1: Char,
    private val c2: Char?,
    private val c3: Char?,
    private val z1: Boolean,
    private val z2: Boolean?,
    private val z3: Boolean?,
    private val s1: String,
    private val s2: String?,
    private val s3: String?,
    private val s4: String? = "",
    private val other1: Other,
    private val other2: Other?,
    private val other3: Other?,
    private val obj1: Obj,
    private val obj2: Obj?,
    private val obj3: Obj?,
    private val enum1: SomeEnum,
    private val enum2: SomeEnum?,
    private val enum3: SomeEnum?,
    @Serializable(with = NotSerializable1Serializer::class) private val notSerializable11: NotSerializable1,
    @Serializable(with = NotSerializable1Serializer::class) private val notSerializable12: NotSerializable1?,
    @Serializable(with = NotSerializable1Serializer::class) private val notSerializable13: NotSerializable1?,
    @Serializable(with = NotSerializable2Serializer::class) private val notSerializable21: NotSerializable2,
    @Serializable(with = NotSerializable2Serializer::class) private val notSerializable22: NotSerializable2?,
    @Serializable(with = NotSerializable2Serializer::class) private val notSerializable23: NotSerializable2?,

    private val intList1: List<Int>,
    private val intList2: List<Int>?,
    private val intList3: List<Int>?,
    private val intList4: List<Int?>,
    private val intList5: List<Int?>?,
    private val intList6: List<Int?>?,

    private val longList1: List<Long>,
    private val longList2: List<Long>?,
    private val longList3: List<Long>?,
    private val longList4: List<Long?>,
    private val longList5: List<Long?>?,
    private val longList6: List<Long?>?,

    private val shortList1: List<Short>,
    private val shortList2: List<Short>?,
    private val shortList3: List<Short>?,
    private val shortList4: List<Short?>,
    private val shortList5: List<Short?>?,
    private val shortList6: List<Short?>?,

    private val byteList1: List<Byte>,
    private val byteList2: List<Byte>?,
    private val byteList3: List<Byte>?,
    private val byteList4: List<Byte?>,
    private val byteList5: List<Byte?>?,
    private val byteList6: List<Byte?>?,

    private val charList1: List<Char>,
    private val charList2: List<Char>?,
    private val charList3: List<Char>?,
    private val charList4: List<Char?>,
    private val charList5: List<Char?>?,
    private val charList6: List<Char?>?,

    private val floatList1: List<Float>,
    private val floatList2: List<Float>?,
    private val floatList3: List<Float>?,
    private val floatList4: List<Float?>,
    private val floatList5: List<Float?>?,
    private val floatList6: List<Float?>?,

    private val doubleList1: List<Double>,
    private val doubleList2: List<Double>?,
    private val doubleList3: List<Double>?,
    private val doubleList4: List<Double?>,
    private val doubleList5: List<Double?>?,
    private val doubleList6: List<Double?>?,

    private val booleanList1: List<Boolean>,
    private val booleanList2: List<Boolean>?,
    private val booleanList3: List<Boolean>?,
    private val booleanList4: List<Boolean?>,
    private val booleanList5: List<Boolean?>?,
    private val booleanList6: List<Boolean?>?,

    private val stringList1: List<String>,
    private val stringList2: List<String>?,
    private val stringList3: List<String>?,
    private val stringList4: List<String?>,
    private val stringList5: List<String?>?,
    private val stringList6: List<String?>?,

    private val serialzableList1: List<Other>,
    private val serialzableList2: List<Other>?,
    private val serialzableList3: List<Other>?,
    private val serialzableList4: List<Other?>,
    private val serialzableList5: List<Other?>?,
    private val serialzableList6: List<Other?>?,

    private val intSet1: Set<Int>,
    private val intSet2: Set<Int>?,
    private val intSet3: Set<Int>?,
    private val intSet4: Set<Int?>,
    private val intSet5: Set<Int?>?,
    private val intSet6: Set<Int?>?,

    private val longSet1: Set<Long>,
    private val longSet2: Set<Long>?,
    private val longSet3: Set<Long>?,
    private val longSet4: Set<Long?>,
    private val longSet5: Set<Long?>?,
    private val longSet6: Set<Long?>?,

    private val shortSet1: Set<Short>,
    private val shortSet2: Set<Short>?,
    private val shortSet3: Set<Short>?,
    private val shortSet4: Set<Short?>,
    private val shortSet5: Set<Short?>?,
    private val shortSet6: Set<Short?>?,

    private val byteSet1: Set<Byte>,
    private val byteSet2: Set<Byte>?,
    private val byteSet3: Set<Byte>?,
    private val byteSet4: Set<Byte?>,
    private val byteSet5: Set<Byte?>?,
    private val byteSet6: Set<Byte?>?,

    private val charSet1: Set<Char>,
    private val charSet2: Set<Char>?,
    private val charSet3: Set<Char>?,
    private val charSet4: Set<Char?>,
    private val charSet5: Set<Char?>?,
    private val charSet6: Set<Char?>?,

    private val floatSet1: Set<Float>,
    private val floatSet2: Set<Float>?,
    private val floatSet3: Set<Float>?,
    private val floatSet4: Set<Float?>,
    private val floatSet5: Set<Float?>?,
    private val floatSet6: Set<Float?>?,

    private val doubleSet1: Set<Double>,
    private val doubleSet2: Set<Double>?,
    private val doubleSet3: Set<Double>?,
    private val doubleSet4: Set<Double?>,
    private val doubleSet5: Set<Double?>?,
    private val doubleSet6: Set<Double?>?,

    private val booleanSet1: Set<Boolean>,
    private val booleanSet2: Set<Boolean>?,
    private val booleanSet3: Set<Boolean>?,
    private val booleanSet4: Set<Boolean?>,
    private val booleanSet5: Set<Boolean?>?,
    private val booleanSet6: Set<Boolean?>?,

    private val stringSet1: Set<String>,
    private val stringSet2: Set<String>?,
    private val stringSet3: Set<String>?,
    private val stringSet4: Set<String?>,
    private val stringSet5: Set<String?>?,
    private val stringSet6: Set<String?>?,

    private val serialzableSet1: Set<Other>,
    private val serialzableSet2: Set<Other>?,
    private val serialzableSet3: Set<Other>?,
    private val serialzableSet4: Set<Other?>,
    private val serialzableSet5: Set<Other?>?,
    private val serialzableSet6: Set<Other?>?,

    private val intMap1: Map<Int, Int>,
    private val intMap2: Map<Int, Int>?,
    private val intMap3: Map<Int, Int>?,
    private val intMap4: Map<Int?, Int?>,
    private val intMap5: Map<Int?, Int?>?,
    private val intMap6: Map<Int?, Int?>?,

    private val longMap1: Map<Long, Long>,
    private val longMap2: Map<Long, Long>?,
    private val longMap3: Map<Long, Long>?,
    private val longMap4: Map<Long?, Long?>,
    private val longMap5: Map<Long?, Long?>?,
    private val longMap6: Map<Long?, Long?>?,

    private val shortMap1: Map<Short, Short>,
    private val shortMap2: Map<Short, Short>?,
    private val shortMap3: Map<Short, Short>?,
    private val shortMap4: Map<Short?, Short?>,
    private val shortMap5: Map<Short?, Short?>?,
    private val shortMap6: Map<Short?, Short?>?,

    private val byteMap1: Map<Byte, Byte>,
    private val byteMap2: Map<Byte, Byte>?,
    private val byteMap3: Map<Byte, Byte>?,
    private val byteMap4: Map<Byte?, Byte?>,
    private val byteMap5: Map<Byte?, Byte?>?,
    private val byteMap6: Map<Byte?, Byte?>?,

    private val charMap1: Map<Char, Char>,
    private val charMap2: Map<Char, Char>?,
    private val charMap3: Map<Char, Char>?,
    private val charMap4: Map<Char?, Char?>,
    private val charMap5: Map<Char?, Char?>?,
    private val charMap6: Map<Char?, Char?>?,

    private val floatMap1: Map<Float, Float>,
    private val floatMap2: Map<Float, Float>?,
    private val floatMap3: Map<Float, Float>?,
    private val floatMap4: Map<Float?, Float?>,
    private val floatMap5: Map<Float?, Float?>?,
    private val floatMap6: Map<Float?, Float?>?,

    private val doubleMap1: Map<Double, Double>,
    private val doubleMap2: Map<Double, Double>?,
    private val doubleMap3: Map<Double, Double>?,
    private val doubleMap4: Map<Double?, Double?>,
    private val doubleMap5: Map<Double?, Double?>?,
    private val doubleMap6: Map<Double?, Double?>?,

    private val booleanMap1: Map<Boolean, Boolean>,
    private val booleanMap2: Map<Boolean, Boolean>?,
    private val booleanMap3: Map<Boolean, Boolean>?,
    private val booleanMap4: Map<Boolean?, Boolean?>,
    private val booleanMap5: Map<Boolean?, Boolean?>?,
    private val booleanMap6: Map<Boolean?, Boolean?>?,

    private val stringMap1: Map<String, String>,
    private val stringMap2: Map<String, String>?,
    private val stringMap3: Map<String, String>?,
    private val stringMap4: Map<String?, String?>,
    private val stringMap5: Map<String?, String?>?,
    private val stringMap6: Map<String?, String?>?,

    private val serializableMap1: Map<Other, Other>,
    private val serializableMap2: Map<Other, Other>?,
    private val serializableMap3: Map<Other, Other>?,
    private val serializableMap4: Map<Other?, Other?>,
    private val serializableMap5: Map<Other?, Other?>?,
    private val serializableMap6: Map<Other?, Other?>?,
) {
    constructor() : this(
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
        notSerializable11 = NotSerializable1(value = 1),
        notSerializable12 = NotSerializable1(value = 2),
        notSerializable13 = null,
        notSerializable21 = NotSerializable2(value = 1),
        notSerializable22 = NotSerializable2(value = 2),
        notSerializable23 = null,

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

        serialzableList1 = listOf(Other(a = 1), Other(a = 2)),
        serialzableList2 = listOf(Other(a = 3), Other(a = 4)),
        serialzableList3 = null,
        serialzableList4 = listOf(Other(a = 5), null),
        serialzableList5 = listOf(Other(a = 6), null),
        serialzableList6 = null,

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

        serialzableSet1 = setOf(Other(a = 1), Other(a = 2)),
        serialzableSet2 = setOf(Other(a = 3), Other(a = 4)),
        serialzableSet3 = null,
        serialzableSet4 = setOf(Other(a = 5), null),
        serialzableSet5 = setOf(Other(a = 6), null),
        serialzableSet6 = null,

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

        booleanMap1 = mapOf(false to true, true to false),
        booleanMap2 = mapOf(true to false, false to true),
        booleanMap3 = null,
        booleanMap4 = mapOf(false to true, null to null),
        booleanMap5 = mapOf(true to false, null to null),
        booleanMap6 = null,

        stringMap1 = mapOf("a" to "A", "b" to "B"),
        stringMap2 = mapOf("c" to "C", "d" to "D"),
        stringMap3 = null,
        stringMap4 = mapOf("e" to "E", null to null),
        stringMap5 = mapOf("f" to "F", null to null),
        stringMap6 = null,

        serializableMap1 = mapOf(Other(a = 1) to Other(a = 11), Other(a = 2) to Other(a = 22)),
        serializableMap2 = mapOf(Other(a = 3) to Other(a = 33), Other(a = 4) to Other(a = 44)),
        serializableMap3 = null,
        serializableMap4 = mapOf(Other(a = 5) to Other(a = 55), null to null),
        serializableMap5 = mapOf(Other(a = 6) to Other(a = 66), null to null),
        serializableMap6 = null,
    )
}

@Serializable
private data class Other(
    val a: Int
) : SomeClass()

@Serializable
private data object Obj

private enum class SomeEnum {
    A, B
}

private interface SomeInterface

private abstract class SomeClass

private data class NotSerializable1(
    val value: Int,
)

private object NotSerializable1Serializer : KSerializer<NotSerializable1> {
    override val descriptor: SerialDescriptor = Int.serializer().descriptor

    override fun serialize(encoder: Encoder, value: NotSerializable1) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): NotSerializable1 =
        NotSerializable1(value = decoder.decodeInt())
}

private data class NotSerializable2(
    val value: Int,
)

private object NotSerializable2Serializer : KSerializer<NotSerializable2> {
    override val descriptor: SerialDescriptor = Int.serializer().descriptor

    override fun serialize(encoder: Encoder, value: NotSerializable2) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): NotSerializable2 =
        NotSerializable2(value = decoder.decodeInt())
}
