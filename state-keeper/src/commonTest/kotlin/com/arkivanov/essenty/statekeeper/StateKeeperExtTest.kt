package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalStateKeeperApi::class)
class StateKeeperExtTest {

    @Test
    fun saveable_holder_saves_and_restores_state() {
        val oldStateKeeper = StateKeeperDispatcher()
        val oldComponent = ComponentWithStateHolder(oldStateKeeper)

        oldComponent.holder.state++

        val savedState = oldStateKeeper.save().serializeAndDeserialize()
        val newStateKeeper = StateKeeperDispatcher(savedState = savedState)
        val newComponent = ComponentWithStateHolder(newStateKeeper)

        assertEquals(1, newComponent.holder.state)
    }

    @Test
    fun saveable_holder_saves_and_restores_nullable_state() {
        val oldStateKeeper = StateKeeperDispatcher()
        val oldComponent = ComponentWithStateHolder(oldStateKeeper)

        oldComponent.nullableHolder.state = 1

        val savedState = oldStateKeeper.save().serializeAndDeserialize()
        val newStateKeeper = StateKeeperDispatcher(savedState = savedState)
        val newComponent = ComponentWithStateHolder(newStateKeeper)

        assertEquals(1, newComponent.nullableHolder.state)
    }

    @Test
    fun saveable_property_saves_and_restores_state() {
        val oldStateKeeper = StateKeeperDispatcher()
        val oldComponent = ComponentWithState(oldStateKeeper)

        oldComponent.state++

        val savedState = oldStateKeeper.save().serializeAndDeserialize()
        val newStateKeeper = StateKeeperDispatcher(savedState = savedState)
        val newComponent = ComponentWithState(newStateKeeper)

        assertEquals(1, newComponent.state)
    }

    @Test
    fun saveable_property_saves_and_restores_nullable_state_1() {
        val oldStateKeeper = StateKeeperDispatcher()
        val oldComponent = ComponentWithState(oldStateKeeper)

        oldComponent.nullableState1 = null

        val savedState = oldStateKeeper.save().serializeAndDeserialize()
        val newStateKeeper = StateKeeperDispatcher(savedState = savedState)
        val newComponent = ComponentWithState(newStateKeeper)

        assertNull(newComponent.nullableState1)
    }

    @Test
    fun saveable_property_saves_and_restores_nullable_state_2() {
        val oldStateKeeper = StateKeeperDispatcher()
        val oldComponent = ComponentWithState(oldStateKeeper)

        oldComponent.nullableState2 = 1

        val savedState = oldStateKeeper.save().serializeAndDeserialize()
        val newStateKeeper = StateKeeperDispatcher(savedState = savedState)
        val newComponent = ComponentWithState(newStateKeeper)

        assertEquals(1, newComponent.nullableState2)
    }

    private class ComponentWithStateHolder(override val stateKeeper: StateKeeper) : StateKeeperOwner {
        val holder by saveable(serializer = Int.serializer(), state = Holder::state) {
            Holder(state = it ?: 0)
        }

        val nullableHolder by saveable(serializer = Int.serializer().nullable, state = NullableHolder::state) {
            NullableHolder(state = it)
        }
    }

    private class ComponentWithState(override val stateKeeper: StateKeeper) : StateKeeperOwner {
        var state: Int by saveable(serializer = Int.serializer()) { 0 }
        var nullableState1: Int? by saveable(serializer = Int.serializer().nullable) { 0 }
        var nullableState2: Int? by saveable(serializer = Int.serializer().nullable) { null }
    }

    private class Holder(var state: Int)
    private class NullableHolder(var state: Int?)
}
