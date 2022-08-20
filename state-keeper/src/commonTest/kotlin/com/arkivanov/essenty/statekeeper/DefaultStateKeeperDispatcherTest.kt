package com.arkivanov.essenty.statekeeper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DefaultStateKeeperDispatcherTest {

    @Test
    fun WHEN_save_recreate_consume_THEN_data_restored() {
        val dispatcher1 =
            DefaultStateKeeperDispatcher(
                savedState = null,
                parcelableContainerFactory = ::TestParcelableContainer
            )

        val parcelable1 = ParcelableStub()
        val parcelable2 = ParcelableStub()

        dispatcher1.register("key1") { parcelable1 }
        dispatcher1.register("key2") { parcelable2 }
        dispatcher1.register("key3") { null }

        val savedState = dispatcher1.save()

        val dispatcher2 =
            DefaultStateKeeperDispatcher(
                savedState = savedState,
                parcelableContainerFactory = ::TestParcelableContainer
            )

        val restoredParcelable1 = dispatcher2.consume("key1", ParcelableStub::class)
        val restoredParcelable2 = dispatcher2.consume("key2", ParcelableStub::class)
        val restoredParcelable3 = dispatcher2.consume("key3", ParcelableStub::class)

        assertEquals(parcelable1, restoredParcelable1)
        assertEquals(parcelable2, restoredParcelable2)
        assertNull(restoredParcelable3)
    }

    @Test
    fun WHEN_consume_second_time_THEN_returns_null() {
        val dispatcher1 =
            DefaultStateKeeperDispatcher(
                savedState = null,
                parcelableContainerFactory = ::TestParcelableContainer
            )

        dispatcher1.register("key") { ParcelableStub() }

        val savedState = dispatcher1.save()

        val dispatcher2 =
            DefaultStateKeeperDispatcher(
                savedState = savedState,
                parcelableContainerFactory = ::TestParcelableContainer
            )

        dispatcher2.consume("key", ParcelableStub::class)

        val restoredParcelable = dispatcher2.consume("key", ParcelableStub::class)

        assertNull(restoredParcelable)
    }

    @Test
    fun GIVEN_not_registered_WHEN_isRegistered_THEN_returns_false() {
        val dispatcher = DefaultStateKeeperDispatcher(savedState = null)

        val result = dispatcher.isRegistered(key = "key")

        assertFalse(result)
    }

    @Test
    fun GIVEN_registered_with_one_key_WHEN_isRegistered_with_another_key_THEN_returns_false() {
        val dispatcher = DefaultStateKeeperDispatcher(savedState = null)
        dispatcher.register(key = "key1") { ParcelableStub() }

        val result = dispatcher.isRegistered(key = "key2")

        assertFalse(result)
    }

    @Test
    fun GIVEN_registered_WHEN_isRegistered_with_same_key_THEN_returns_true() {
        val dispatcher = DefaultStateKeeperDispatcher(savedState = null)
        dispatcher.register(key = "key") { ParcelableStub() }

        val result = dispatcher.isRegistered(key = "key")

        assertTrue(result)
    }
}
