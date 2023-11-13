package com.arkivanov.essenty.lifecycle

/**
 * The callbacks of the [Lifecycle] with addition [onStateChanged] callback for [Lifecycle.Event] listening.
 */
abstract class LifecycleEventCallbacks : Lifecycle.Callbacks {

    abstract fun onStateChanged(lifecycleEvent: Lifecycle.Event)

    override fun onCreate(): Unit = onStateChanged(Lifecycle.Event.ON_CREATE)

    override fun onStart(): Unit = onStateChanged(Lifecycle.Event.ON_START)

    override fun onResume(): Unit = onStateChanged(Lifecycle.Event.ON_RESUME)

    override fun onPause(): Unit = onStateChanged(Lifecycle.Event.ON_PAUSE)

    override fun onStop(): Unit = onStateChanged(Lifecycle.Event.ON_STOP)

    override fun onDestroy(): Unit = onStateChanged(Lifecycle.Event.ON_DESTROY)

}
