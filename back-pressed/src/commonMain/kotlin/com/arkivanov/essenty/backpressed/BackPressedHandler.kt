package com.arkivanov.essenty.backpressed

interface BackPressedHandler {

    fun register(handler: () -> Boolean)

    fun unregister(handler: () -> Boolean)
}
