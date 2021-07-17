package com.arkivanov.essenty.backpressed

import com.arkivanov.essenty.utils.internal.ensureNeverFrozen

internal class DefaultBackPressedDispatcher : BackPressedDispatcher {

    init {
        ensureNeverFrozen()
    }

    private var handlers = emptySet<() -> Boolean>()

    override fun register(handler: () -> Boolean) {
        this.handlers += handler
    }

    override fun unregister(handler: () -> Boolean) {
        this.handlers -= handler
    }

    override fun onBackPressed(): Boolean = handlers.reversed().any { it() }
}
