package com.arkivanov.essenty.parcelable

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("TestFunctionName")
class SimpleParcelableContainerTest {

    @Test
    fun GIVEN_value_not_set_WHEN_consume_THEN_returns_null() {
        val container = SimpleParcelableContainer()

        val consumedValue = container.consume(Some::class)

        assertNull(consumedValue)
    }

    @Test
    fun WHEN_value_set_and_consume_THEN_returns_value() {
        val container = SimpleParcelableContainer()
        val value = Some(value = "value")

        container.set(value)
        val consumedValue = container.consume(Some::class)

        assertEquals(value, consumedValue)
    }

    @Test
    fun WHEN_value_set_and_set_null_and_consume_THEN_returns_value() {
        val container = SimpleParcelableContainer()

        container.set(Some(value = "value"))
        container.set(null)
        val consumedValue = container.consume(Some::class)

        assertNull(consumedValue)
    }

    @Parcelize
    private data class Some(val value: String) : Parcelable
}
