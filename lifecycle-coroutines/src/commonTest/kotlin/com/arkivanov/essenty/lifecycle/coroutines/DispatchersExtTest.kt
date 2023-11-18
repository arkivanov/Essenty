package com.arkivanov.essenty.lifecycle.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("TestFunctionName")
class DispatchersExtTest {

    @AfterTest
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun WHEN_immediateOrDefault_called_multiple_times_THEN_returns_same_dispatcher() {
        val dispatcher1 = Dispatchers.Main.immediateOrFallback
        val dispatcher2 = Dispatchers.Main.immediateOrFallback

        assertSame(dispatcher1, dispatcher2)
    }

    @Test
    fun GIVEN_Main_dispatcher_changed_WHEN_immediateOrDefault_called_THEN_returns_updated_dispatcher() {
        try {
            Dispatchers.Main.immediate
        } catch (e: NotImplementedError) {
            return // Only test on platforms where Main dispatcher is supported
        }

        val oldDispatcher = Dispatchers.Main.immediateOrFallback
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        val newDispatcher = Dispatchers.Main.immediateOrFallback

        assertNotSame(oldDispatcher, newDispatcher)
    }
}
