package com.arkivanov.essenty.lifecycle.reaktive

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.disposable.scope.DisposableScope

/**
 * Creates and returns a new [DisposableScope], which is automatically
 * disposed when the [Lifecycle] is destroyed.
 */
fun LifecycleOwner.disposableScope(): DisposableScope =
    DisposableScope().withLifecycle(lifecycle)

/**
 * Automatically disposes this [Disposable] when the specified [lifecycle] is destroyed.
 *
 * @return the same (this) [Disposable].
 */
fun <T : Disposable> T.withLifecycle(lifecycle: Lifecycle): T {
    lifecycle.doOnDestroy(::dispose)

    return this
}
