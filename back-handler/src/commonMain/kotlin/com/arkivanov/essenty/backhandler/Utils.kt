package com.arkivanov.essenty.backhandler

internal fun Iterable<BackCallback>.findMostImportant(): BackCallback? =
    sortedBy(BackCallback::priority).lastOrNull(BackCallback::isEnabled)
