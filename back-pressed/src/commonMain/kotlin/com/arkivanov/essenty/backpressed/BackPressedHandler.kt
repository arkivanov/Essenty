package com.arkivanov.essenty.backpressed

@Deprecated("Use BackHandler from back-handler module.")
interface BackPressedHandler {

    fun register(handler: () -> Boolean)

    fun unregister(handler: () -> Boolean)
}
