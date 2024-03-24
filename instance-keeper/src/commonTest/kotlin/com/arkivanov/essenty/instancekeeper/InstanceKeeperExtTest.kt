package com.arkivanov.essenty.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.SimpleInstance
import kotlin.test.Test
import kotlin.test.assertSame

@Suppress("TestFunctionName")
class InstanceKeeperExtTest {

    private val dispatcher = InstanceKeeperDispatcher()

    @Test
    fun WHEN_provide_with_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.provide(key = "key") { SimpleInstance(Thing()) }.instance
        val thing2 = dispatcher.provide(key = "key") { SimpleInstance(Thing()) }.instance

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_provide_without_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.provide { SimpleInstance(Thing()) }.instance
        val thing2 = dispatcher.provide { SimpleInstance(Thing()) }.instance

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_provideSimple_with_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.provideSimple(key = "key", factory = ::Thing)
        val thing2 = dispatcher.provideSimple(key = "key", factory = ::Thing)

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_provideSimple_without_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.provideSimple(factory = ::Thing)
        val thing2 = dispatcher.provideSimple(factory = ::Thing)

        assertSame(thing1, thing2)
    }

    private class Thing
}
