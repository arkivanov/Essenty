package com.arkivanov.essenty.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.Lifecycle as EssentyLifecycle

fun Lifecycle.asEssentyLifecycle(): EssentyLifecycle = EssentyLifecycleInterop(this)

private class EssentyLifecycleInterop(
    private val delegate: Lifecycle
) : EssentyLifecycle {

    private var observerMap = HashMap<EssentyLifecycle.Callbacks, LifecycleObserver>()

    override val state: EssentyLifecycle.State get() = delegate.currentState.toEssentyLifecycleState()

    override fun subscribe(callbacks: EssentyLifecycle.Callbacks) {
        check(callbacks !in observerMap) { "Already subscribed" }

        val observer = AndroidLifecycleObserver(callbacks)
        this.observerMap[callbacks] = observer
        delegate.addObserver(observer)
    }

    override fun unsubscribe(callbacks: EssentyLifecycle.Callbacks) {
        val observer = observerMap.remove(callbacks)

        check(observer != null) { "Not subscribed" }

        delegate.removeObserver(observer)
    }
}

private fun Lifecycle.State.toEssentyLifecycleState(): EssentyLifecycle.State =
    when (this) {
        Lifecycle.State.DESTROYED -> EssentyLifecycle.State.DESTROYED
        Lifecycle.State.INITIALIZED -> EssentyLifecycle.State.INITIALIZED
        Lifecycle.State.CREATED -> EssentyLifecycle.State.CREATED
        Lifecycle.State.STARTED -> EssentyLifecycle.State.STARTED
        Lifecycle.State.RESUMED -> EssentyLifecycle.State.RESUMED
    }

private class AndroidLifecycleObserver(
    private val delegate: EssentyLifecycle.Callbacks
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        delegate.onCreate()
    }

    override fun onStart(owner: LifecycleOwner) {
        delegate.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        delegate.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        delegate.onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        delegate.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        delegate.onDestroy()
    }
}
