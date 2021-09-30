package com.arkivanov.essenty.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.Lifecycle as EssentyLifecycle

/**
 * Converts AndroidX [Lifecycle] to Essenty [Lifecycle][EssentyLifecycle]
 */
fun Lifecycle.asEssentyLifecycle(): EssentyLifecycle = EssentyLifecycleInterop(this)

/**
 * Converts AndroidX [Lifecycle] to Essenty [Lifecycle][EssentyLifecycle]
 */
fun LifecycleOwner.essentyLifecycle(): EssentyLifecycle = lifecycle.asEssentyLifecycle()

private class EssentyLifecycleInterop(
    private val delegate: Lifecycle
) : EssentyLifecycle {

    private var observerMap = HashMap<EssentyLifecycle.Callbacks, LifecycleObserver>()

    override val state: EssentyLifecycle.State get() = delegate.currentState.toEssentyLifecycleState()

    override fun subscribe(callbacks: EssentyLifecycle.Callbacks) {
        check(callbacks !in observerMap) { "Already subscribed" }

        val observer = AndroidLifecycleObserver(delegate = callbacks, onDestroy = { observerMap -= callbacks })
        observerMap[callbacks] = observer
        delegate.addObserver(observer)
    }

    override fun unsubscribe(callbacks: EssentyLifecycle.Callbacks) {
        observerMap.remove(callbacks)?.also {
            delegate.removeObserver(it)
        }
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
    private val delegate: EssentyLifecycle.Callbacks,
    private val onDestroy: () -> Unit,
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
        onDestroy.invoke()
    }
}
