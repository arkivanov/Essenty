package com.arkivanov.essenty.statekeeper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

        val savedState = dispatcher1.save()

        val dispatcher2 =
            DefaultStateKeeperDispatcher(
                savedState = savedState,
                parcelableContainerFactory = ::TestParcelableContainer
            )

        val restoredParcelable1 = dispatcher2.consume("key1", ParcelableStub::class)
        val restoredParcelable2 = dispatcher2.consume("key2", ParcelableStub::class)

        assertEquals(parcelable1, restoredParcelable1)
        assertEquals(parcelable2, restoredParcelable2)
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
}
