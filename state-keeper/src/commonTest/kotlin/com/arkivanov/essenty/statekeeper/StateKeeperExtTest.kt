package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import kotlinx.serialization.builtins.serializer
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalEssentyApi::class)
class StateKeeperExtTest {

    @Test
    fun withSavedState_saves_and_restores_state() {
        val oldDispatcher = StateKeeperDispatcher()

        val holder =
            oldDispatcher.withSavedState(key = "SAVED_STATE", serializer = Int.serializer(), state = Holder::state) {
                Holder(state = it ?: 0)
            }

        holder.state++

        val savedState = oldDispatcher.save().serializeAndDeserialize()
        val newDispatcher = StateKeeperDispatcher(savedState = savedState)

        val newHolder =
            newDispatcher.withSavedState(key = "SAVED_STATE", serializer = Int.serializer(), state = Holder::state) {
                Holder(state = it ?: 0)
            }

        assertEquals(1, newHolder.state)
    }

    private class Holder(var state: Int)
}
