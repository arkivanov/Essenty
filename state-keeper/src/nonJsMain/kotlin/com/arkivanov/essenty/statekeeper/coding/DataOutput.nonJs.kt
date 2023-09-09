package com.arkivanov.essenty.statekeeper.coding

internal actual fun DataOutput.writeFloat(value: Float) {
    writeInt(value.toBits())
}
