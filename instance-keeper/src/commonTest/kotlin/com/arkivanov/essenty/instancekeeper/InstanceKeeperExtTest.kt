package com.arkivanov.essenty.instancekeeper

import kotlin.test.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame

@Suppress("TestFunctionName", "DEPRECATION")
@OptIn(ExperimentalInstanceKeeperApi::class)
class InstanceKeeperExtTest {

    private val dispatcher = InstanceKeeperDispatcher()

    @Test
    fun WHEN_getOrCreate_with_same_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreate(key = "key") { ThingInstance<Int>() }
        val thing2 = dispatcher.getOrCreate(key = "key") { ThingInstance<Int>() }

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreate_with_different_key_called_second_time_THEN_returns_new_instance() {
        val thing1 = dispatcher.getOrCreate(key = "key1") { ThingInstance<Int>() }
        val thing2 = dispatcher.getOrCreate(key = "key2") { ThingInstance<Int>() }

        assertNotSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreate_with_same_type_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreate { ThingInstance<Int>() }
        val thing2 = dispatcher.getOrCreate { ThingInstance<Int>() }

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreate_with_different_type_called_second_time_THEN_returns_new_instance() {
        val thing1 = dispatcher.getOrCreate { ThingInstance<Int>() }
        val thing2 = dispatcher.getOrCreate { ThingInstance<Float>() }

        assertNotSame<ThingInstance<*>>(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreateSimple_with_same_key_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreateSimple(key = "key") { Thing<Int>() }
        val thing2 = dispatcher.getOrCreateSimple(key = "key") { Thing<Int>() }

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreateSimple_with_different_key_called_second_time_THEN_returns_new_instance() {
        val thing1 = dispatcher.getOrCreateSimple(key = "key1") { Thing<Int>() }
        val thing2 = dispatcher.getOrCreateSimple(key = "key2") { Thing<Int>() }

        assertNotSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreateSimple_with_same_type_called_second_time_THEN_returns_same_instance() {
        val thing1 = dispatcher.getOrCreateSimple { Thing<Int>() }
        val thing2 = dispatcher.getOrCreateSimple { Thing<Int>() }

        assertSame(thing1, thing2)
    }

    @Test
    fun WHEN_getOrCreateSimple_with_different_type_called_second_time_THEN_returns_new_instance() {
        val thing1 = dispatcher.getOrCreateSimple { Thing<Int>() }
        val thing2 = dispatcher.getOrCreateSimple { Thing<Float>() }

        assertNotSame<Thing<*>>(thing1, thing2)
    }

    @Test
    fun retainingInstance_retains_instance() {
        val instanceKeeper = InstanceKeeperDispatcher()
        val component1 = Component(instanceKeeper)

        val component2 = Component(instanceKeeper)

        assertSame(component1.instance, component2.instance)
    }

    @Test
    fun retainingSimpleInstance_retains_instance() {
        val instanceKeeper = InstanceKeeperDispatcher()
        val component1 = Component(instanceKeeper)

        val component2 = Component(instanceKeeper)

        assertSame(component1.simpleInstance, component2.simpleInstance)
    }

    @Test
    fun retainingClosable_retains_instance() {
        val instanceKeeper = InstanceKeeperDispatcher()
        val component1 = Component(instanceKeeper)

        val component2 = Component(instanceKeeper)

        assertSame(component1.closeable, component2.closeable)
    }

    @Suppress("unused")
    private class Thing<out T>

    @Suppress("unused")
    private class ThingInstance<out T> : InstanceKeeper.Instance

    @Suppress("unused")
    private class ThingCloseable<out T> : AutoCloseable {
        override fun close() {
            // no-op
        }
    }

    private class Component(override val instanceKeeper: InstanceKeeper) : InstanceKeeperOwner {
        val instance by retainingInstance { ThingInstance<Int>() }
        val simpleInstance by retainingSimpleInstance { Thing<Int>() }
        val closeable by retainingCloseable { ThingCloseable<Int>() }
    }
}
