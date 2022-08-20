package com.arkivanov.essenty.lifecycle

import kotlin.js.JsName

/**
 * Represents [Lifecycle] and [Lifecycle.Callbacks] at the same time.
 * Can be used to manually control the [Lifecycle].
 */
interface LifecycleRegistry : Lifecycle, Lifecycle.Callbacks

/**
 * Creates a default implementation of [LifecycleRegistry].
 */
@JsName("lifecycleRegistry")
fun LifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(initialState = Lifecycle.State.INITIALIZED)

/**
 * Creates a default implementation of [LifecycleRegistry] with the specified [initialState].
 */
fun LifecycleRegistry(
    initialState: Lifecycle.State,
): LifecycleRegistry =
    LifecycleRegistryImpl(initialState)
