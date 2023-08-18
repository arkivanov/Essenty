package com.arkivanov.essenty.parcelable

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertNull

@Suppress("TestFunctionName")
@RunWith(RobolectricTestRunner::class)
class IgnoredOnParcelTest {

    @Test
    fun WHEN_parcelling_ignored_THEN_returns_default_value() {
        val value = Ignored(ignored = "value")

        val restoredValue = value.serializeAndDeserialize()

        assertNull(restoredValue.ignored)
    }

    @Parcelize
    private data class Ignored(@IgnoredOnParcel val ignored: String? = null) : Parcelable
}
