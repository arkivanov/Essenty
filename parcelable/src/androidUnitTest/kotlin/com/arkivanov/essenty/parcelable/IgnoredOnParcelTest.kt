package com.arkivanov.essenty.parcelable

import android.os.Parcel
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Suppress("TestFunctionName")
@RunWith(RobolectricTestRunner::class)
class IgnoredOnParcelTest {

    @Test
    fun WHEN_parcelling_ignored_THEN_returns_default_value() {
        val parcel = Parcel.obtain()
        val value = Ignored(ignored = "value")

        parcel.writeParcelable(value, 0)
        parcel.setDataPosition(0)
        val restoredValue = parcel.readParcelable<Ignored>(Ignored::class.java.classLoader)

        assertNotNull(restoredValue)
        assertNull(restoredValue.ignored)
    }

    @Parcelize
    private data class Ignored(@IgnoredOnParcel val ignored: String? = null) : Parcelable
}
