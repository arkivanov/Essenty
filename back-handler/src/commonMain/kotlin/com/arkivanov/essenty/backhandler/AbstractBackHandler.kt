package com.arkivanov.essenty.backhandler

import com.arkivanov.essenty.backhandler.BackHandler.Callback

internal abstract class AbstractBackHandler : BackHandler {

    private var set = emptySet<Callback>()
    private val enabledChangedListener: (Boolean) -> Unit = { onEnabledChanged() }

    override fun register(callback: Callback) {
        check(callback !in set) { "Callback is already registered" }

        this.set += callback
        callback.addEnabledChangedListener(enabledChangedListener)
        onEnabledChanged()
    }

    override fun unregister(callback: Callback) {
        check(callback in set) { "Callback is not registered" }

        callback.removeEnabledChangedListener(enabledChangedListener)
        this.set -= callback
        onEnabledChanged()
    }

    private fun onEnabledChanged() {
        onEnabledChanged(set.any(Callback::isEnabled))
    }

    protected abstract fun onEnabledChanged(isEnabled: Boolean)

    protected fun callCallbacks(): Boolean {
        set.lastOrNull(Callback::isEnabled)?.also {
            it.callback()
            return true
        }

        return false
    }
}
