package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

private const val KEY_STATE = "STATE_KEEPER_STATE"

/**
 * Creates a new instance of [StateKeeper] and attaches it to the provided AndroidX [SavedStateRegistry].
 *
 * @param savedStateRegistry a [SavedStateRegistry] to attach the returned [StateKeeper] to.
 * @param discardSavedState a flag indicating whether any previously saved state should be discarded or not,
 * default value is `false`.
 * @param isSavingAllowed called before saving the state.
 * When `true` then the state will be saved, otherwise it won't. Default value is `true`.
 */
fun StateKeeper(
    savedStateRegistry: SavedStateRegistry,
    discardSavedState: Boolean = false,
    isSavingAllowed: () -> Boolean = { true },
): StateKeeper =
    StateKeeper(
        savedStateRegistry = savedStateRegistry,
        key = KEY_STATE,
        discardSavedState = discardSavedState,
        isSavingAllowed = isSavingAllowed,
    )

/**
 * Creates a new instance of [StateKeeper] and attaches it to the provided AndroidX [SavedStateRegistry].
 *
 * @param savedStateRegistry a [SavedStateRegistry] to attach the returned [StateKeeper] to.
 * @param key a key to access the provided [SavedStateRegistry], to be used by the returned [StateKeeper].
 * @param discardSavedState a flag indicating whether any previously saved state should be discarded or not,
 * default value is `false`.
 * @param isSavingAllowed called before saving the state.
 * When `true` then the state will be saved, otherwise it won't. Default value is `true`.
 */
fun StateKeeper(
    savedStateRegistry: SavedStateRegistry,
    key: String,
    discardSavedState: Boolean = false,
    isSavingAllowed: () -> Boolean = { true },
): StateKeeper {
    val dispatcher =
        StateKeeperDispatcher(
            savedState = savedStateRegistry
                .consumeRestoredStateForKey(key = key)
                ?.getSerializableContainer(key = KEY_STATE)
                ?.takeUnless { discardSavedState },
        )

    savedStateRegistry.registerSavedStateProvider(key = key) {
        Bundle().apply {
            if (isSavingAllowed()) {
                putSerializableContainer(key = KEY_STATE, value = dispatcher.save())
            }
        }
    }

    return dispatcher
}

/**
 * Creates a new instance of [StateKeeper] and attaches it to the AndroidX [SavedStateRegistry].
 *
 * @param discardSavedState a flag indicating whether any previously saved state should be discarded or not,
 * default value is `false`.
 * @param isSavingAllowed called before saving the state.
 * When `true` then the state will be saved, otherwise it won't. Default value is `true`.
 */
fun SavedStateRegistryOwner.stateKeeper(
    discardSavedState: Boolean = false,
    isSavingAllowed: () -> Boolean = { true },
): StateKeeper =
    stateKeeper(
        key = KEY_STATE,
        discardSavedState = discardSavedState,
        isSavingAllowed = isSavingAllowed,
    )

/**
 * Creates a new instance of [StateKeeper] and attaches it to the AndroidX [SavedStateRegistry].
 *
 * @param key a key to access this [SavedStateRegistry], to be used by the returned [StateKeeper].
 * @param discardSavedState a flag indicating whether any previously saved state should be discarded or not,
 * default value is `false`.
 * @param isSavingAllowed called before saving the state.
 * When `true` then the state will be saved, otherwise it won't. Default value is `true`.
 */
fun SavedStateRegistryOwner.stateKeeper(
    key: String,
    discardSavedState: Boolean = false,
    isSavingAllowed: () -> Boolean = { true },
): StateKeeper =
    StateKeeper(
        savedStateRegistry = savedStateRegistry,
        key = key,
        discardSavedState = discardSavedState,
        isSavingAllowed = isSavingAllowed
    )
