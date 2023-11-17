package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Start [Flow] collecting when [Lifecycle.State] is at least as [minActiveState].
 * It stopped collecting if "opposite" [Lifecycle.State] will appear.
 */
fun <T> Flow<T>.withLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = callbackFlow {
    lifecycle.repeatOnLifecycle(minActiveState) {
        this@withLifecycle.collect {
            send(it)
        }
    }
    close()
}

@Deprecated(
    message = "Use 'withLifecycle' instead",
    replaceWith = ReplaceWith("withLifecycle(lifecycle, minActiveState)"),
    level = DeprecationLevel.ERROR
)
fun <T> Flow<T>.flowWithLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> {
    return withLifecycle(lifecycle, minActiveState)
}
