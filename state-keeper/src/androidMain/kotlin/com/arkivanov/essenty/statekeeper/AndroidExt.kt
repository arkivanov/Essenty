package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.arkivanov.essenty.parcelable.ParcelableContainer

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
    isSavingAllowed: () -> Boolean = { true }
): StateKeeper {
    val dispatcher =
        StateKeeperDispatcher(
            savedState = savedStateRegistry
                .consumeRestoredStateForKey(KEY_STATE)
                ?.getParcelable<ParcelableContainer>(KEY_STATE)
                ?.takeUnless { discardSavedState },
        )

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
