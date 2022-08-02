package com.arkivanov.essenty.backhandler

import com.arkivanov.essenty.utils.internal.ensureNeverFrozen

internal class DefaultBackDispatcher : AbstractBackHandler(), BackDispatcher {

    init {
        ensureNeverFrozen()
    }

    override var isEnabled: Boolean = false

    override fun onEnabledChanged(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }

    override fun back(): Boolean = callCallbacks()
}
