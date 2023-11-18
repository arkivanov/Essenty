package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.CoroutineContext

/**
 * [Flow] operator that emits values from this upstream [Flow] when the [lifecycle]
 * is at least at [minActiveState] state. The emissions will be stopped when the
 * [lifecycle] state falls below [minActiveState] state.
 *
 * The [Flow] is collected on the specified [context], which defaults to
 * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate]
 * if available on the current platform, or to [Dispatchers.Main] otherwise.
 *
 * See the [AndroidX documentation](https://developer.android.com/reference/kotlin/androidx/lifecycle/package-summary#(kotlinx.coroutines.flow.Flow).flowWithLifecycle(androidx.lifecycle.Lifecycle,androidx.lifecycle.Lifecycle.State))
 * for more information.
 */
fun <T> Flow<T>.withLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = Dispatchers.Main.immediateOrFallback,
): Flow<T> = callbackFlow {
    lifecycle.repeatOnLifecycle(minActiveState, context) {
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
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = Dispatchers.Main.immediateOrFallback,
): Flow<T> {
    return withLifecycle(lifecycle, minActiveState, context)
}
