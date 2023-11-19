package com.arkivanov.essenty.lifecycle

import com.arkivanov.essenty.lifecycle.Lifecycle.Callbacks
import com.arkivanov.essenty.lifecycle.Lifecycle.State
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class LifecycleExtTest {
    private val owner = TestLifecycleOwner()
    private val events = ArrayList<String>()

    @Test
    fun WHEN_doOnCreate_THEN_not_called() {
        State.entries.forEach { state ->
            owner.state = state
            owner.doOnCreate(callback())
        }

        assertNoEvents()
    }

    @Test
    fun WHEN_doOnCreate_and_onCreate_called_THEN_called() {
        owner.doOnCreate(callback())
        owner.call(Callbacks::onCreate)

        assertOneEvent()
    }

    @Test
    fun WHEN_doOnStart_THEN_not_called() {
        State.entries.forEach { state ->
            owner.state = state
            owner.doOnStart(block = callback())
        }

        assertNoEvents()
    }

    @Test
    fun WHEN_doOnStart_and_onStart_called_multiple_times_THEN_called_multiple_times() {
        owner.doOnStart(block = callback())
        owner.call(Callbacks::onStart)
        owner.call(Callbacks::onStart)

        assertEquals(2, events.size)
    }

    @Test
    fun WHEN_doOnStart_isOneTime_true_and_onStart_called_multiple_times_THEN_called_once() {
        owner.doOnStart(isOneTime = true, block = callback())
        owner.call(Callbacks::onStart)
        owner.call(Callbacks::onStart)

        assertOneEvent()
    }

    @Test
    fun WHEN_doOnResume_THEN_not_called() {
        State.entries.forEach { state ->
            owner.state = state
            owner.doOnResume(block = callback())
        }

        assertNoEvents()
    }

    @Test
    fun WHEN_doOnResume_and_onResume_called_multiple_times_THEN_called_multiple_times() {
        owner.doOnResume(block = callback())
        owner.call(Callbacks::onResume)
        owner.call(Callbacks::onResume)

        assertEquals(2, events.size)
    }

    @Test
    fun WHEN_doOnResume_isOneTime_true_and_onResume_called_multiple_times_THEN_called_once() {
        owner.doOnResume(isOneTime = true, block = callback())
        owner.call(Callbacks::onResume)
        owner.call(Callbacks::onResume)

        assertOneEvent()
    }

    @Test
    fun WHEN_doOnPause_THEN_not_called() {
        State.entries.forEach { state ->
            owner.state = state
            owner.doOnPause(block = callback())
        }

        assertNoEvents()
    }

    @Test
    fun WHEN_doOnPause_and_onPause_called_multiple_times_THEN_called_multiple_times() {
        owner.doOnPause(block = callback())
        owner.call(Callbacks::onPause)
        owner.call(Callbacks::onPause)

        assertEquals(2, events.size)
    }

    @Test
    fun WHEN_doOnPause_isOneTime_true_and_onPause_called_multiple_times_THEN_called_once() {
        owner.doOnPause(isOneTime = true, block = callback())
        owner.call(Callbacks::onPause)
        owner.call(Callbacks::onPause)

        assertOneEvent()
    }

    @Test
    fun WHEN_doOnStop_THEN_not_called() {
        State.entries.forEach { state ->
            owner.state = state
            owner.doOnStop(block = callback())
        }

        assertNoEvents()
    }

    @Test
    fun WHEN_doOnStop_and_onStop_called_multiple_times_THEN_called_multiple_times() {
        owner.doOnStop(block = callback())
        owner.call(Callbacks::onStop)
        owner.call(Callbacks::onStop)

        assertEquals(2, events.size)
    }

    @Test
    fun WHEN_doOnStop_isOneTime_true_and_onStop_called_multiple_times_THEN_called_once() {
        owner.doOnStop(isOneTime = true, block = callback())
        owner.call(Callbacks::onStop)
        owner.call(Callbacks::onStop)

        assertOneEvent()
    }

    @Test
    fun GIVEN_state_INITIALIZED_WHEN_doOnDestroy_THEN_not_called() {
        owner.doOnDestroy(callback())

        assertNoEvents()
    }

    @Test
    fun GIVEN_state_CREATED_WHEN_doOnDestroy_THEN_not_called() {
        owner.state = State.CREATED

        owner.doOnDestroy(callback())

        assertNoEvents()
    }

    @Test
    fun GIVEN_state_CREATED_WHEN_doOnDestroy_and_onDestroy_called_THEN_called() {
        owner.state = State.CREATED

        owner.doOnDestroy(callback())
        owner.call(Callbacks::onDestroy)

        assertOneEvent()
    }

    private fun assertNoEvents() {
        assertContentEquals(emptyList(), events)
    }

    private fun assertOneEvent() {
        assertEquals(1, events.size)
    }

    private fun callback(name: String = "event"): () -> Unit =
        { events += name }

    private class TestLifecycleOwner : LifecycleOwner {
        override val lifecycle: TestLifecycle = TestLifecycle()

        var state: State by lifecycle::state

        fun call(call: (Callbacks) -> Unit) {
            lifecycle.callbacks.forEach(call)
        }
    }

    private class TestLifecycle : Lifecycle {
        override var state: State = State.INITIALIZED
        var callbacks: MutableSet<Callbacks> = HashSet()

        override fun subscribe(callbacks: Callbacks) {
            this.callbacks += callbacks
        }

        override fun unsubscribe(callbacks: Callbacks) {
            this.callbacks -= callbacks
        }
    }
}
