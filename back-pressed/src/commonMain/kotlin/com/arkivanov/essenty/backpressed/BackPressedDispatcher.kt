package com.arkivanov.essenty.backpressed

import kotlin.js.JsName

@Deprecated("Use BackDispatcher from back-handler module.")
interface BackPressedDispatcher : BackPressedHandler {

    fun onBackPressed(): Boolean
}

@Deprecated(
    "Use BackDispatcher from back-handler module.",
    ReplaceWith("BackDispatcher()"),
)
@JsName("backPressedDispatcher")
@Suppress("FunctionName")
fun BackPressedDispatcher(): BackPressedDispatcher = DefaultBackPressedDispatcher()
