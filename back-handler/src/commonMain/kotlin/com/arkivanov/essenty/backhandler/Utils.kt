package com.arkivanov.essenty.backhandler

internal fun Iterable<BackCallback>.call(): Boolean {
    lastOrNull(BackCallback::isEnabled)?.also {
        it.onBack()
        return true
    }

    return false
}
