@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.arkivanov.essenty.statekeeper.benchmarks

import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.arkivanov.essenty.statekeeper.deserialize
import com.arkivanov.essenty.statekeeper.serialize
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.time.measureTime

//@RunWith(RobolectricTestRunner::class)
@Config(minSdk = Build.VERSION_CODES.TIRAMISU)
class Benchmarks {

    // Manual run only
//    @Test
    fun size() {
        val data = getData()
        println("Parcelable size: ${data.getParcelizedSize()}")
        println("Serializable size: ${data.getSerializedSize()}")

        val newDataParcelable = data.parcelize().deparcelize()
        val newDataSerializable = data.serialize(Data.serializer()).deserialize(Data.serializer())

        assertEquals(data, newDataParcelable)
        assertEquals(data, newDataSerializable)
    }

    // Manual run only
//    @Test
    fun performance() {
        val data = getData()

        repeat(100) {
            data.parcelize().deparcelize()
            data.serialize(Data.serializer()).deserialize(Data.serializer())
        }

        val t1 =
            measureTime {
                repeat(100) {
                    data.parcelize().deparcelize()
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

    private fun Data.getParcelizedSize(): Int =
        parcelize().size

    private fun Data.parcelize(): ByteArray {
        val bundle = Bundle()
        bundle.putParcelable("key", this)
        val parcel = Parcel.obtain()
        parcel.writeBundle(bundle)
        return parcel.marshall()
    }

    private fun ByteArray.deparcelize(): Data {
        val parcel = Parcel.obtain()
        parcel.unmarshall(this, 0, size)
        parcel.setDataPosition(0)

        return requireNotNull(parcel.readBundle()).getParcelable("key", Data::class.java)!!
    }

    private fun Data.getSerializedSize(): Int =
        serialize(Data.serializer()).size

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
