package com.arkivanov.essenty.backpressed

interface BackPressedRegistry {

    fun register(handler: () -> Boolean)

    fun unregister(handler: () -> Boolean)
}
