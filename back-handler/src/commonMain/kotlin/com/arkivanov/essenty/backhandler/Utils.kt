package com.arkivanov.essenty.backhandler

internal fun Map<() -> Unit, Boolean>.call(): Boolean {
    entries.lastOrNull { it.value }?.also {
        it.key()
        return true
    }

    return false
}

internal fun MutableMap<() -> Unit, Boolean>.asCallbacks(onChanged: () -> Unit = {}): BackHandler.Callbacks =
    CallbacksFromMap(map = this, onChanged = onChanged)

private class CallbacksFromMap(
    private val map: MutableMap<() -> Unit, Boolean>,
    private val onChanged: () -> Unit,
) : BackHandler.Callbacks {

    override fun get(callback: () -> Unit): Boolean? = map[callback]

    override fun put(callback: () -> Unit, isEnabled: Boolean) {
        if (map[callback] != isEnabled) {
            map[callback] = isEnabled
            onChanged()
        }
    }

    override fun remove(callback: () -> Unit) {
        if (callback in map) {
            map -= callback
            onChanged()
        }
    }
}
