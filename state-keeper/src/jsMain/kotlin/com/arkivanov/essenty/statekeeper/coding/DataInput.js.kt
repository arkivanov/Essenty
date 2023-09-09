package com.arkivanov.essenty.statekeeper.coding

internal actual fun DataInput.readFloat(): Float =
    readDouble().toFloat()
