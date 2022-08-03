package com.arkivanov.essenty.backhandler

import com.arkivanov.essenty.utils.internal.ensureNeverFrozen

internal class DefaultBackDispatcher : BackDispatcher {

    init {
        ensureNeverFrozen()
    }

    private var set = emptySet<BackCallback>()
    override val isEnabled: Boolean get() = set.any(BackCallback::isEnabled)

    override fun register(callback: BackCallback) {
        check(callback !in set) { "Callback is already registered" }

        this.set += callback
    }

    override fun unregister(callback: BackCallback) {
        check(callback in set) { "Callback is not registered" }

        this.set -= callback
    }

    override fun back(): Boolean = set.call()
}
