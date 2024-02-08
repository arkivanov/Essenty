package com.arkivanov.essenty.backhandler

internal class DefaultBackDispatcher : BackDispatcher {

    private var set = emptySet<BackCallback>()
    private var progressCallback: BackCallback? = null
    override val isEnabled: Boolean get() = set.any(BackCallback::isEnabled)
    private var enabledChangedListeners = emptySet<(Boolean) -> Unit>()
    private var hasEnabledCallback: Boolean = false
    private val onCallbackEnabledChanged: (Boolean) -> Unit = { onCallbackEnabledChanged() }

    private fun onCallbackEnabledChanged() {
        val hasEnabledCallback = isEnabled
        if (this.hasEnabledCallback != hasEnabledCallback) {
            this.hasEnabledCallback = hasEnabledCallback
            enabledChangedListeners.forEach { it.invoke(hasEnabledCallback) }
        }
    }

    override fun register(callback: BackCallback) {
        check(callback !in set) { "Callback is already registered" }

        this.set += callback
        callback.addEnabledChangedListener(onCallbackEnabledChanged)
        onCallbackEnabledChanged()
    }

    override fun unregister(callback: BackCallback) {
        check(callback in set) { "Callback is not registered" }

        this.set -= callback
        callback.removeEnabledChangedListener(onCallbackEnabledChanged)

        if (callback == progressCallback) {
            progressCallback = null
        }

        onCallbackEnabledChanged()
    }

    override fun addEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit) {
        enabledChangedListeners += listener
    }

    override fun removeEnabledChangedListener(listener: (isEnabled: Boolean) -> Unit) {
        enabledChangedListeners -= listener
    }

    override fun back(): Boolean {
        val callback = progressCallback ?: set.findMostImportant()
        progressCallback = null
        callback?.onBack()

        return callback != null
    }

    override fun startPredictiveBack(backEvent: BackEvent): Boolean {
        val callback = set.findMostImportant() ?: return false
        progressCallback = callback
        callback.onBackStarted(backEvent)

        return true
    }

    override fun progressPredictiveBack(backEvent: BackEvent) {
        progressCallback?.onBackProgressed(backEvent)
    }

    override fun cancelPredictiveBack() {
        progressCallback?.onBackCancelled()
        progressCallback = null
    }
}
