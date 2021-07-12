package com.arkivanov.essenty.lifecycle

import kotlin.js.JsName

interface LifecycleRegistry : Lifecycle, Lifecycle.Callbacks

@JsName("lifecycleRegistry")
@Suppress("FunctionName") // Factory function
fun LifecycleRegistry(): LifecycleRegistry = LifecycleRegistryImpl()
