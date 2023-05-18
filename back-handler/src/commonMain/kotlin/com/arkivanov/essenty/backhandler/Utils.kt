package com.arkivanov.essenty.backhandler

internal fun Iterable<BackCallback>.findMostImportant(): BackCallback? =
    sortedBy(BackCallback::priority).lastOrNull(BackCallback::isEnabled)

internal fun Iterable<BackCallback>.call(): Boolean {
    findMostImportant()?.also {
        it.onBack()
        return true
    }

    return false
}
