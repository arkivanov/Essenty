package com.arkivanov.essenty.lifecycle

import kotlin.js.JsName

interface LifecycleRegistry : Lifecycle, Lifecycle.Callbacks

@JsName("lifecycleRegistry")
fun LifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(initialState = Lifecycle.State.INITIALIZED)

fun LifecycleRegistry(
    initialState: Lifecycle.State,
): LifecycleRegistry =
    LifecycleRegistryImpl(initialState)
