package com.arkivanov.essenty.lifecycle

import kotlin.jvm.JvmStatic

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

    /**
     * Define the possible events of the [Lifecycle] that mirrored [State]
     */
    enum class Event {
        ON_CREATE,
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        ON_STOP,
        ON_DESTROY;

        companion object {

            @JvmStatic
            fun downFrom(state: State): Event? = when (state) {
                State.CREATED -> ON_DESTROY
                State.STARTED -> ON_STOP
                State.RESUMED -> ON_PAUSE
                else -> null
            }

            @JvmStatic
            fun upTo(state: State): Event? = when (state) {
                State.CREATED -> ON_CREATE
                State.STARTED -> ON_START
                State.RESUMED -> ON_RESUME
                else -> null
            }
        }
    }
}
