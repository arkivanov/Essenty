package com.arkivanov.essenty.parcelable

import android.os.Parcel
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("TestFunctionName")
@RunWith(RobolectricTestRunner::class)
class AndroidParcelableContainerRobolectricTest {

    @Test
    fun WHEN_parcelling_THEN_preserves_value() {
        val value = Some(value = "value")
        var container = AndroidParcelableContainer()

        container.set(value)
        container = container.recreate()
        val consumedValue = container.consume(Some::class)

        assertEquals(value, consumedValue)
    }

    @Test
    fun WHEN_parcelling_without_consumption_THEN_preserves_value() {
        val value = Some(value = "value")
        var container = AndroidParcelableContainer()

        container.set(value)
        container = container.recreate()
        container = container.recreate()
        val consumedValue = container.consume(Some::class)

        assertEquals(value, consumedValue)
    }

    @Test
    fun GIVEN_parcelled_WHEN_consume_second_time_THEN_returns_null() {
        var container = AndroidParcelableContainer()
        container.set(Some(value = "value"))
        container = container.recreate()

        container.consume(Some::class)
        val consumedValue = container.consume(Some::class)

        assertNull(consumedValue)
    }

    private fun AndroidParcelableContainer.recreate(): AndroidParcelableContainer {
        val parcel = Parcel.obtain()
        writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        return AndroidParcelableContainer.CREATOR.createFromParcel(parcel)
    }

    @Parcelize
    private data class Some(val value: String) : Parcelable
}
