package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import kotlinx.serialization.Serializable
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class BundleExtTest {

    @Test
    fun getSerializable_returns_same_value_after_putSerializable_without_serialization() {
        val value = Value(value = "123")
        val bundle = Bundle()
        bundle.putSerializable(key = "key", value = value, strategy = Value.serializer())
        val newValue = bundle.getSerializable(key = "key", strategy = Value.serializer())

        assertEquals(value, newValue)
    }

    @Test
    fun getSerializable_returns_same_value_after_putSerializable_with_serialization() {
        val value = Value(value = "123")
        val bundle = Bundle()
        bundle.putSerializable(key = "key", value = value, strategy = Value.serializer())
        val newValue = bundle.parcelize().deparcelize().getSerializable(key = "key", strategy = Value.serializer())

        assertEquals(value, newValue)
    }

    @Test
    fun getSerializable_returns_same_value_after_putSerializable_with_double_serialization() {
        val value = Value(value = "123")
        val bundle = Bundle()
        bundle.putSerializable(key = "key", value = value, strategy = Value.serializer())
        bundle.putInt("int", 123)
        val newBundle = bundle.parcelize().deparcelize()
        newBundle.getInt("int") // Force partial deserialization of the Bundle
        val newValue = newBundle.parcelize().deparcelize().getSerializable(key = "key", strategy = Value.serializer())

        assertEquals(value, newValue)
    }

    @Serializable
    data class Value(val value: String)
}
