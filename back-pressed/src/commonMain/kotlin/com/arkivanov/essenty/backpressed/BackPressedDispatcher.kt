package com.arkivanov.essenty.backpressed

import kotlin.js.JsName

interface BackPressedDispatcher : BackPressedHandler {

    fun onBackPressed(): Boolean
}

@JsName("backPressedDispatcher")
@Suppress("FunctionName")
fun BackPressedDispatcher(): BackPressedDispatcher = DefaultBackPressedDispatcher()
