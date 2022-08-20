package com.arkivanov.essenty.lifecycle

/**
 * A holder of [Lifecycle.State] that can be observed for changes.
 *
 * Possible transitions:
 *
 * ```
 * [INITIALIZED] ──┐
 *                 ↓
 *         ┌── [CREATED] ──┐
 *         ↓       ↑       ↓
 *    [DESTROYED]  └── [STARTED] ──┐
 *                         ↑       ↓
 *                         └── [RESUMED]
 * ```
 */
interface Lifecycle {

    /**
     * The current state of the [Lifecycle].
     */
    val state: State

    /**
     * Subscribes the given [callbacks] to state changes.
     */
    fun subscribe(callbacks: Callbacks)

    /**
     * Unsubscribes the given [callbacks] from state changes.
     */
    fun unsubscribe(callbacks: Callbacks)

    /**
     * Defines the possible states of the [Lifecycle].
     */
    enum class State {
        DESTROYED,
        INITIALIZED,
        CREATED,
        STARTED,
        RESUMED
    }

    /**
     * The callbacks of the [Lifecycle]. Each callback is called on the corresponding state change.
     */
    interface Callbacks {
        fun onCreate() {
        }

        fun onStart() {
        }

        fun onResume() {
        }

        fun onPause() {
        }

        fun onStop() {
        }

        fun onDestroy() {
        }
    }
}
