package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * Creates and returns a new [CoroutineScope] with the specified [context].
 * The returned [CoroutineScope] is automatically cancelled when the [Lifecycle] is destroyed.
 */
fun LifecycleOwner.coroutineScope(
    context: CoroutineContext = Dispatchers.Main.immediateOrFallback,
): CoroutineScope =
    CoroutineScope(context = context).withLifecycle(lifecycle)

/**
 * Automatically cancels this [CoroutineScope] when the specified [lifecycle] is destroyed.
 *
 * @return the same (this) [CoroutineScope].
 */
fun CoroutineScope.withLifecycle(lifecycle: Lifecycle): CoroutineScope {
    if (lifecycle.state == Lifecycle.State.DESTROYED) {
        cancel()
    } else {
        lifecycle.doOnDestroy(::cancel)
    }

    return this
}
