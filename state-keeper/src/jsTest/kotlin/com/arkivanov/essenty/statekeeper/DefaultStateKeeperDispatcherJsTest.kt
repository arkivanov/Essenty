package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.ParcelableContainer
import kotlin.test.Test
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DefaultStateKeeperDispatcherJsTest {

    // Verifies the workaround for https://youtrack.jetbrains.com/issue/KT-49186
    @Test
    fun WHEN_save_THEN_returns_ParcelableContainer() {
        val stateKeeper = DefaultStateKeeperDispatcher(null)

        val parcelableContainer = stateKeeper.save()

        @Suppress("USELESS_IS_CHECK")
        assertTrue(parcelableContainer is ParcelableContainer)
    }
}
