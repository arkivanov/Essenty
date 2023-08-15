package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class AbstractParcelizeTest {

    @Test
    fun parcelize() {
        val data = Data()

        val copy = serializeAndDeserialize(data)

        assertEquals(data, copy)
    }

    protected abstract fun serializeAndDeserialize(data: Data): Data

    @TypeParceler<Bar, BarParceler>
    @Parcelize
    protected data class Data(
        val z: Boolean = true,
        val i: Int = 1,
        val l: Long = 1L,
        val f: Float = 1F,
        val d: Double = 1.0,
        val b: Byte = 1,
        val s: Short = 1,
        val str: String = "str",
        val some: Some = Some(),
        val foo: @WriteWith<FooParceler> Foo = Foo(),
        val bar: Bar = Bar(),
    ) : Parcelable

    @Parcelize
    protected data class Some(
        val i: Int = 1,
        val d: Double = 1.0,
        val s: String = "s",
    ) : Parcelable

    protected data class Foo(
        val i: Int = 1,
        val d: Double = 1.0,
        val s: String = "s",
    )

    @OptIn(ExperimentalEssentyApi::class)
    private object FooParceler : CommonParceler<Foo> {
        override fun Foo.write(writer: ParcelWriter) {
            writer.writeInt(i)
            writer.writeDouble(d)
            writer.writeString(s)
        }

        override fun create(reader: ParcelReader): Foo =
            Foo(
                i = reader.readInt(),
                d = reader.readDouble(),
                s = reader.readString(),
            )
    }

    protected data class Bar(
        val i: Int = 1,
        val d: Double = 1.0,
        val s: String = "s",
    )

    @OptIn(ExperimentalEssentyApi::class)
    private object BarParceler : CommonParceler<Bar> {
        override fun Bar.write(writer: ParcelWriter) {
            writer.writeInt(i)
            writer.writeDouble(d)
            writer.writeString(s)
        }

        override fun create(reader: ParcelReader): Bar =
            Bar(
                i = reader.readInt(),
                d = reader.readDouble(),
                s = reader.readString(),
            )
    }
}
