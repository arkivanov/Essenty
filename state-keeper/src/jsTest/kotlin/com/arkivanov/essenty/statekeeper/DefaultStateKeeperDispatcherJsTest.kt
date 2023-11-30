package com.arkivanov.essenty.statekeeper

import kotlin.test.Test
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DefaultStateKeeperDispatcherJsTest {

    // Verifies the workaround for https://youtrack.jetbrains.com/issue/KT-49186
    @Test
    fun WHEN_save_THEN_returns_SerializableContainer() {
        val stateKeeper = DefaultStateKeeperDispatcher(null)

        val serializableContainer = stateKeeper.save()

        @Suppress("USELESS_IS_CHECK")
        assertTrue(serializableContainer is SerializableContainer)
    }
}
