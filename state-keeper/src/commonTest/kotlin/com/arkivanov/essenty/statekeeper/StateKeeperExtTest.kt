package com.arkivanov.essenty.statekeeper

import kotlinx.serialization.builtins.serializer
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun saveable_property_saves_and_restores_state() {
        val oldStateKeeper = StateKeeperDispatcher()
        val oldComponent = ComponentWithState(oldStateKeeper)

        oldComponent.state++

        val savedState = oldStateKeeper.save().serializeAndDeserialize()
        val newStateKeeper = StateKeeperDispatcher(savedState = savedState)
        val newComponent = ComponentWithState(newStateKeeper)

        assertEquals(1, newComponent.state)
    }

    private class ComponentWithStateHolder(stateKeeper: StateKeeper) {
        val holder by stateKeeper.saveable(serializer = Int.serializer(), state = Holder::state) {
            Holder(state = it ?: 0)
        }
    }

    private class ComponentWithState(stateKeeper: StateKeeper) {
        var state by stateKeeper.saveable(serializer = Int.serializer()) { 0 }
    }

    private class Holder(var state: Int)
}
