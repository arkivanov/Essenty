package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import kotlinx.serialization.KSerializer

/**
 * Helper function for creating objects with a saved state.
 *
 * @param key a key for saving and restoring the state.
 * @param serializer a [KSerializer] for serializing and deserializing the state.
 * @param state a function that selects a state [S] from the resulting
 * object [T] and returns it for saving.
 * @param block a function that accepts the previously saved state [S] (if any) and
 * returns an object of type [T].
 * @return an object of type [T] returned by [block] function.
 */
@ExperimentalEssentyApi
inline fun <T, S : Any> StateKeeper.withSavedState(
    key: String,
    serializer: KSerializer<S>,
    crossinline state: (T) -> S,
    block: (savedState: S?) -> T,
): T {
    val result = block(consume(key = key, strategy = serializer))
    register(key = key, strategy = serializer) { state(result) }

    return result
}

/**
 * A convenience function for [StateKeeper.withSavedState].
 */
@ExperimentalEssentyApi
inline fun <T, S : Any> StateKeeperOwner.withSavedState(
    key: String,
    serializer: KSerializer<S>,
    crossinline state: (T) -> S,
    block: (savedState: S?) -> T,
): T =
    stateKeeper.withSavedState(
        key = key,
        serializer = serializer,
        state = state,
        block = block,
    )
