package com.arkivanov.essenty.backhandler

internal class DefaultBackDispatcher : BackDispatcher {

    private var set = emptySet<BackCallback>()
    private var progressData: ProgressData? = null
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

    override fun isRegistered(callback: BackCallback): Boolean =
        callback in set

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

        if (callback == progressData?.callback) {
            progressData?.callback = null
            callback.onBackCancelled()
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
        val callback = progressData?.callback ?: set.findMostImportant()
        progressData = null
        callback?.onBack()

        return callback != null
    }

    override fun startPredictiveBack(backEvent: BackEvent): Boolean {
        val callback = set.findMostImportant() ?: return false
        progressData = ProgressData(startEvent = backEvent, callback = callback)
        callback.onBackStarted(backEvent)

        return true
    }

    override fun progressPredictiveBack(backEvent: BackEvent) {
        val progressData = progressData ?: return

        if (progressData.callback == null) {
            progressData.callback = set.findMostImportant()
            progressData.callback?.onBackStarted(progressData.startEvent)
        }

        progressData.callback?.onBackProgressed(backEvent)
    }

    override fun cancelPredictiveBack() {
        progressData?.callback?.onBackCancelled()
        progressData = null
    }

    private class ProgressData(
        val startEvent: BackEvent,
        var callback: BackCallback?,
    )
}
