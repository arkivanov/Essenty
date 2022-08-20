package com.arkivanov.essenty.lifecycle

/**
 * Drives the state of the [Lifecycle] forward to [Lifecycle.State.CREATED].
 * Does nothing if the state is already [Lifecycle.State.CREATED] or greater, or [Lifecycle.State.DESTROYED].
 */
fun LifecycleRegistry.create() {
    if (state == Lifecycle.State.INITIALIZED) {
        onCreate()
    }
}

/**
 * Drives the state of the [Lifecycle] forward  to [Lifecycle.State.STARTED].
 * Does nothing if the state is already [Lifecycle.State.STARTED] or greater, or [Lifecycle.State.DESTROYED].
 */
fun LifecycleRegistry.start() {
    create()

    if (state == Lifecycle.State.CREATED) {
        onStart()
    }
}

/**
 * Drives the state of the [Lifecycle] forward  to [Lifecycle.State.RESUMED].
 * Does nothing if the state is already [Lifecycle.State.RESUMED] or greater, or [Lifecycle.State.DESTROYED].
 */
fun LifecycleRegistry.resume() {
    start()

    if (state == Lifecycle.State.STARTED) {
        onResume()
    }
}

/**
 * Drives the state of the [Lifecycle] backward to [Lifecycle.State.STARTED].
 * Does nothing if the state is already [Lifecycle.State.STARTED] or lower.
 */
fun LifecycleRegistry.pause() {
    if (state == Lifecycle.State.RESUMED) {
        onPause()
    }
}

/**
 * Drives the state of the [Lifecycle] backward to [Lifecycle.State.CREATED].
 * Does nothing if the state is already [Lifecycle.State.CREATED] or lower.
 */
fun LifecycleRegistry.stop() {
    pause()

    if (state == Lifecycle.State.STARTED) {
        onStop()
    }
}

/**
 * Drives the state of the [Lifecycle] backward to [Lifecycle.State.DESTROYED].
 * Does nothing if the state is already [Lifecycle.State.DESTROYED].
 */
fun LifecycleRegistry.destroy() {
    stop()

    if (state == Lifecycle.State.CREATED) {
        onDestroy()
    }
}
