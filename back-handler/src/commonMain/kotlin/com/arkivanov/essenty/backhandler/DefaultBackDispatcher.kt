package com.arkivanov.essenty.backhandler

import com.arkivanov.essenty.backhandler.BackHandler.Callbacks
import com.arkivanov.essenty.utils.internal.ensureNeverFrozen

internal class DefaultBackDispatcher : BackDispatcher {

    init {
        ensureNeverFrozen()
    }

    private val map = mutableMapOf<() -> Unit, Boolean>()
    override val callbacks: Callbacks = map.asCallbacks()
    override val isEnabled: Boolean get() = map.values.any { it }

    override fun back(): Boolean = map.call()
}
