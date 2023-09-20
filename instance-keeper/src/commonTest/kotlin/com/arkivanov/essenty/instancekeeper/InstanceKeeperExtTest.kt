package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.SimpleInstance
import kotlin.test.Test
import kotlin.test.assertSame

@Suppress("TestFunctionName")
class InstanceKeeperExtTest {

    private val dispatcher = InstanceKeeperDispatcher()

    @Test
    fun WHEN_getOrCreate_with_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreate(key = "key") { SimpleInstance(Thing()) }.instance
        val thing2 = dispatcher.getOrCreate(key = "key") { SimpleInstance(Thing()) }.instance

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreate_without_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreate { SimpleInstance(Thing()) }.instance
        val thing2 = dispatcher.getOrCreate { SimpleInstance(Thing()) }.instance

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreateSimple_with_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreateSimple(key = "key", factory = ::Thing)
        val thing2 = dispatcher.getOrCreateSimple(key = "key", factory = ::Thing)

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreateSimple_without_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreateSimple(factory = ::Thing)
        val thing2 = dispatcher.getOrCreateSimple(factory = ::Thing)

        assertSame(thing1, thing2)
    }

    private class Thing
}
