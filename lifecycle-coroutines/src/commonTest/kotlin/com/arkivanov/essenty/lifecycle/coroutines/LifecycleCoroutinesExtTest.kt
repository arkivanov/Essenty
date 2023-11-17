package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
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
        val state = Lifecycle.State.CREATED
        val expected = listOf(state)

        val actual = executeRepeatOnEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_passed_state_STARTED_must_be_trigger_block_twice() = runTest {
        val state = Lifecycle.State.STARTED
        val expected = listOf(state, state)

        val actual = executeRepeatOnEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_passed_state_RESUMED_must_be_trigger_block_twice() = runTest {
        val state = Lifecycle.State.RESUMED
        val expected = listOf(state, state)

        val actual = executeRepeatOnEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_passed_state_DESTROYED_must_not_be_trigger_block() = runTest {
        val state = Lifecycle.State.DESTROYED
        val expected = emptyList<Lifecycle.State>()

        val actual = executeRepeatOnEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expected, actual)
    }

    @Test
    fun test_flow_passed_state_CREATED_must_be_trigger_block_once() = runTest {
        val state = Lifecycle.State.CREATED
        val expect = listOf(state, state)

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    @Test
    fun test_flow_passed_state_STARTED_must_be_trigger_block_twice() = runTest {
        val state = Lifecycle.State.STARTED
        val expect = listOf(state, state, state, state)

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    @Test
    fun test_flow_passed_state_RESUMED_must_be_trigger_block_twice() = runTest {
        val state = Lifecycle.State.RESUMED
        val expect = listOf(state, state, state, state)

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    @Test
    fun test_flow_passed_state_DESTROYED_must_not_be_trigger_block() = runTest {
        val state = Lifecycle.State.DESTROYED
        val expect = emptyList<Lifecycle.State>()

        val actual = executeFlowWithEssentyLifecycleTest(state)
        advanceUntilIdle()

        assertEquals(expect, actual)
    }

    private suspend fun executeRepeatOnEssentyLifecycleTest(
        lifecycleState: Lifecycle.State
    ): List<Lifecycle.State> = coroutineScope {
        val events = ArrayList<Lifecycle.State>()

        launch {
            registry.repeatOnLifecycle(
                minActiveState = lifecycleState,
                coroutineDispatcher = testDispatcher
            ) {
                events.add(lifecycleState)
            }
        }

        registry.onCreate()
        yield()
        registry.onStart()
        yield()
        registry.onResume()
        yield()
        registry.onPause()
        yield()
        registry.onStop()
        yield()
        registry.onStart()
        yield()
        registry.onResume()
        yield()
        registry.onPause()
        yield()
        registry.onStop()
        yield()
        registry.onDestroy()

        return@coroutineScope events
    }

    private suspend fun executeFlowWithEssentyLifecycleTest(
        lifecycleState: Lifecycle.State
    ): List<Lifecycle.State> = coroutineScope {
        val actual = ArrayList<Lifecycle.State>()

        launch {
            flow {
                repeat(2) { emit(lifecycleState) }
            }
                .withLifecycle(registry, lifecycleState)
                .collect { actual.add(it) }
        }


        registry.onCreate()
        yield()
        registry.onStart()
        yield()
        registry.onResume()
        yield()
        yield()
        registry.onPause()
        yield()
        registry.onStop()
        yield()
        registry.onStart()
        yield()
        registry.onResume()
        yield()
        registry.onPause()
        yield()
        registry.onStop()
        yield()
        registry.onDestroy()

        return@coroutineScope actual
    }
}
