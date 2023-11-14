package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LifecycleCoroutinesExtTest {

    private val testDispatcher = StandardTestDispatcher()
    private val registry = LifecycleRegistry(initialState = Lifecycle.State.INITIALIZED)

    @BeforeTest
    fun beforeTesting() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun afterTesting() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_passed_state_CREATED_must_be_trigger_block_once() = runTest {
        val testState = Lifecycle.State.CREATED
        val expected = listOf(testState.name)

        val actual = executeRepeatOnEssentyLifecycleTest(testState)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_passed_state_STARTED_must_be_trigger_block_twice() = runTest {
        val testState = Lifecycle.State.STARTED
        val expected = listOf(testState.name, testState.name)

        val actual = executeRepeatOnEssentyLifecycleTest(testState)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_passed_state_RESUMED_must_be_trigger_block_twice() = runTest {
        val testState = Lifecycle.State.RESUMED
        val expected = listOf(testState.name, testState.name)

        val actual = executeRepeatOnEssentyLifecycleTest(testState)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_passed_state_DESTROYED_must_not_be_trigger_block() = runTest {
        val testState = Lifecycle.State.DESTROYED
        val expected = emptyList<String>()

        val actual = executeRepeatOnEssentyLifecycleTest(testState)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_flow_passed_state_CREATED_must_be_trigger_block_once() = runTest {
        val state = Lifecycle.State.CREATED
        val expect = listOf(state.name, state.name)

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    @Test
    fun test_flow_passed_state_STARTED_must_be_trigger_block_twice() = runTest {
        val state = Lifecycle.State.STARTED
        val expect = listOf(state.name, state.name, state.name, state.name)

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    @Test
    fun test_flow_passed_state_RESUMED_must_be_trigger_block_twice() = runTest {
        val state = Lifecycle.State.RESUMED
        val expect = listOf(state.name, state.name, state.name, state.name)

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    @Test
    fun test_flow_passed_state_DESTROYED_must_not_be_trigger_block() = runTest {
        val state = Lifecycle.State.DESTROYED
        val expect = emptyList<String>()

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    private suspend fun executeRepeatOnEssentyLifecycleTest(
        lifecycleState: Lifecycle.State,
        onEventAppears: () -> String = { lifecycleState.name }
    ): List<String> = coroutineScope {
        val events = ArrayList<String>()

        launch {
            registry.repeatOnEssentyLifecycle(
                state = lifecycleState
            ) {
                events.add(onEventAppears())
            }
        }

        registry.onCreate()
        delay(10)
        registry.onStart()
        delay(10)
        registry.onResume()
        delay(10)
        registry.onPause()
        delay(10)
        registry.onStop()
        delay(10)
        registry.onStart()
        delay(10)
        registry.onResume()
        delay(10)
        registry.onPause()
        delay(10)
        registry.onStop()
        delay(10)
        registry.onDestroy()

        return@coroutineScope events
    }

    private suspend fun executeFlowWithEssentyLifecycleTest(
        lifecycleState: Lifecycle.State,
        onEventAppears: () -> String = { lifecycleState.name }
    ): List<String> = coroutineScope {
        val actual = ArrayList<String>()

        launch {
            flow {
                repeat(2) { emit(onEventAppears()) }
            }
                .flowWithEssentyLifecycle(registry, lifecycleState)
                .onEach { actual.add(it) }
                .launchIn(this)
        }


        registry.onCreate()
        delay(10)
        registry.onStart()
        delay(10)
        registry.onResume()
        delay(10)
        registry.onPause()
        delay(10)
        registry.onStop()
        delay(10)
        registry.onStart()
        delay(10)
        registry.onResume()
        delay(10)
        registry.onPause()
        delay(10)
        registry.onStop()
        delay(10)
        registry.onDestroy()

        return@coroutineScope actual
    }
}
