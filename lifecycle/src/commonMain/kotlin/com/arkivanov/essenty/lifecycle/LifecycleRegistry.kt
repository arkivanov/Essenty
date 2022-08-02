package com.arkivanov.essenty.lifecycle

import kotlin.js.JsName

interface LifecycleRegistry : Lifecycle, Lifecycle.Callbacks

@Deprecated(message = "Hidden for binary compatibility", level = DeprecationLevel.HIDDEN)
@JsName("lifecycleRegistryDeprecated")
fun LifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(initialState = Lifecycle.State.INITIALIZED)

@JsName("lifecycleRegistry")
@Suppress("FunctionName") // Factory function
fun LifecycleRegistry(
    initialState: Lifecycle.State = Lifecycle.State.INITIALIZED,
): LifecycleRegistry =
    LifecycleRegistryImpl(initialState)
