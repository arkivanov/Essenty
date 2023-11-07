package com.arkivanov.essenty.backhandler

import com.arkivanov.essenty.backhandler.BackDispatcher.PredictiveBackDispatcher

internal class DefaultBackDispatcher : BackDispatcher {

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

    override fun startPredictiveBack(backEvent: BackEvent): PredictiveBackDispatcher? {
        val callback = set.findMostImportant() ?: return null

        callback.onBackStarted(backEvent)

        return object : PredictiveBackDispatcher {
            override fun progress(backEvent: BackEvent) {
                callback.onBackProgressed(backEvent)
            }

            override fun finish() {
                callback.onBack()
            }

            override fun cancel() {
                callback.onBackCancelled()
            }
        }
    }
}
