package com.arkivanov.essenty.statekeeper

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

//@RunWith(RobolectricTestRunner::class)
class Benchmarks {

    // Manual run only
//    @Test
    fun size() {
        val data = getData()
        println("Parcelable size: ${data.getSerializedSize()}")
        println("Serializable size: ${data.getSerializedSize(Data.serializer())}")

        val newData = data.serialize(Data.serializer()).deserialize(Data.serializer())

        assertEquals(data, newData)
    }

    // Manual run only
//    @Test
    fun performance() {
        val data = getData()

        repeat(100) {
            data.serialize().deserialize<Data>()
            data.serialize(Data.serializer()).deserialize(Data.serializer())
        }

        val t1 =
            measureTimeMillis {
                repeat(100) {
                    data.serialize().deserialize<Data>()
                }
            }

        val t2 =
            measureTimeMillis {
                repeat(100) {
                    data.serialize(Data.serializer()).deserialize(Data.serializer())
                }
            }

        println("Parcelize ms: $t1")
        println("Serialize ms: $t2")
    }

    private fun Parcelable.serialize(): ByteArray {
        val parcel = Parcel.obtain()
        parcel.writeParcelable(this, 0)
        return parcel.marshall()
    }

    private inline fun <reified T : Parcelable> ByteArray.deserialize(): T {
        val parcel = Parcel.obtain()
        parcel.unmarshall(this, 0, size)
        parcel.setDataPosition(0)

        @Suppress("DEPRECATION")
        return requireNotNull(parcel.readParcelable(T::class.java.classLoader)) as T
    }

    private fun Parcelable.getSerializedSize(): Int =
        serialize().size

    private fun <T : Any> T.getSerializedSize(strategy: SerializationStrategy<T>): Int =
        serialize(strategy).size

    private fun getData(): Data =
        getInnerData(
            dataList = List(30) {
                getInnerData(
                    dataList = List(10) {
                        getInnerData()
                    },
                )
            },
        )

    private fun getInnerData(dataList: List<Data> = emptyList()): Data =
        Data(
            booleanValue = true,
            byteValue = 64,
            shortValue = 8192,
            integerValue = 1234567,
            longValue = 12345678912345,
            floatValue = 100F,
            doubleValue = 100.0,
            charValue = 'a',
            stringValue = "Some string data goes here",
            intList = List(100) { it },
            stringList = List(100) { "Some string data goes here" },
            dataList = dataList,
        )

    @Serializable
    @Parcelize
    data class Data(
        val booleanValue: Boolean,
        val byteValue: Byte,
        val shortValue: Short,
        val integerValue: Int,
        val longValue: Long,
        val floatValue: Float,
        val doubleValue: Double,
        val charValue: Char,
        val stringValue: String,
        val intList: List<Int>,
        val stringList: List<String>,
        val dataList: List<Data>,
    ) : Parcelable
}
