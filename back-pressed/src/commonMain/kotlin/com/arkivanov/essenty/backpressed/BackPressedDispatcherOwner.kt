package com.arkivanov.essenty.backpressed

@Deprecated("Use BackHandlerOwner from back-handler module.")
interface BackPressedDispatcherOwner {

    val backPressedDispatcher: BackPressedDispatcher
}
