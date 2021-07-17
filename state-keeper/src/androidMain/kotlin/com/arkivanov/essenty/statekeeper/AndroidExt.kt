package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

private const val KEY_STATE = "STATE_KEEPER_STATE"

/**
 * Creates a new instance of [StateKeeper] and attaches it to the provided AndroidX [SavedStateRegistry].
 *
 * @param isSavingAllowed called before saving the state.
 * When `true` then the state will be saved, otherwise it won't. Default value is `true`.
 */
@Suppress("FunctionName") // Factory function
fun StateKeeper(
    savedStateRegistry: SavedStateRegistry,
    isSavingAllowed: () -> Boolean = { true }
): StateKeeper {
    val dispatcher = StateKeeperDispatcher(savedStateRegistry.consumeRestoredStateForKey(KEY_STATE)?.getParcelable(KEY_STATE))

    savedStateRegistry.registerSavedStateProvider(KEY_STATE) {
        Bundle().apply {
            if (isSavingAllowed()) {
                putParcelable(KEY_STATE, dispatcher.save())
            }
        }
    }

    return dispatcher
}

/**
 * Creates a new instance of [StateKeeper] and attaches it to the AndroidX [SavedStateRegistry].
 *
 * @param isSavingAllowed called before saving the state.
 * When `true` then the state will be saved, otherwise it won't. Default value is `true`.
 */
fun SavedStateRegistryOwner.stateKeeper(isSavingAllowed: () -> Boolean = { true }): StateKeeper =
    StateKeeper(
        savedStateRegistry = savedStateRegistry,
        isSavingAllowed = isSavingAllowed
    )
