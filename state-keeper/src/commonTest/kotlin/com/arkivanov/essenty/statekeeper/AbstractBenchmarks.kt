package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.time.measureTime

abstract class AbstractBenchmarks<D> {

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
            data.serialize().deserialize(Data::class)
            data.serialize(Data.serializer()).deserialize(Data.serializer())
        }

        val t1 =
            measureTime {
                repeat(100) {
                    data.serialize().deserialize(Data::class)
                }
            }

        val t2 =
            measureTime {
                repeat(100) {
                    data.serialize(Data.serializer()).deserialize(Data.serializer())
                }
            }

        println("Parcelize time: $t1")
        println("Serialize time: $t2")
    }

    protected abstract fun Parcelable.serialize(): D

    protected abstract fun <T : Parcelable> D.deserialize(clazz: KClass<T>): T

    protected abstract fun D.getSizeBytes(): Int

    private fun Parcelable.getSerializedSize(): Int =
        serialize().getSizeBytes()

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
