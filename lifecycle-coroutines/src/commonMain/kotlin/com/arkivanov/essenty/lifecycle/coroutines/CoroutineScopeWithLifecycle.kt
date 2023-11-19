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
 *
 * @param context a [CoroutineContext] to be used for creating the [CoroutineScope], default
 * is [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate]
 * if available on the current platform, or [Dispatchers.Main] otherwise.
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
    lifecycle.doOnDestroy(::cancel)

    return this
}
