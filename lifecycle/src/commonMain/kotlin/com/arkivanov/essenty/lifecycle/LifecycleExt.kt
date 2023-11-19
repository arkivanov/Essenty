package com.arkivanov.essenty.lifecycle

/**
 * A convenience method for [Lifecycle.subscribe].
 */
fun Lifecycle.subscribe(
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null
): Lifecycle.Callbacks =
    object : Lifecycle.Callbacks {
        override fun onCreate() {
            onCreate?.invoke()
        }

        override fun onStart() {
            onStart?.invoke()
        }

        override fun onResume() {
            onResume?.invoke()
        }

        override fun onPause() {
            onPause?.invoke()
        }

        override fun onStop() {
            onStop?.invoke()
        }

        override fun onDestroy() {
            onDestroy?.invoke()
        }
    }.also(::subscribe)

/**
 * Registers the callback [block] to be called when this [Lifecycle] is created.
 */
inline fun Lifecycle.doOnCreate(crossinline block: () -> Unit) {
    subscribe(
        object : Lifecycle.Callbacks {
            override fun onCreate() {
                unsubscribe(this)
                block()
            }
        }
    )
}

/**
 * Registers the callback [block] to be called when this [Lifecycle] is started.
 *
 * @param isOneTime if `true` then the callback is automatically unregistered right before
 * the first call, default value is `false`.
 */
inline fun Lifecycle.doOnStart(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    subscribe(
        object : Lifecycle.Callbacks {
            override fun onStart() {
                if (isOneTime) {
                    unsubscribe(this)
                }

                block()
            }
        }
    )
}

/**
 * Registers the callback [block] to be called when this [Lifecycle] is resumed.
 *
 * @param isOneTime if `true` then the callback is automatically unregistered right before
 * the first call, default value is `false`.
 */
inline fun Lifecycle.doOnResume(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    subscribe(
        object : Lifecycle.Callbacks {
            override fun onResume() {
                if (isOneTime) {
                    unsubscribe(this)
                }

                block()
            }
        }
    )
}

/**
 * Registers the callback [block] to be called when this [Lifecycle] is paused.
 *
 * @param isOneTime if `true` then the callback is automatically unregistered right before
 * the first call, default value is `false`.
 */
inline fun Lifecycle.doOnPause(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    subscribe(
        object : Lifecycle.Callbacks {
            override fun onPause() {
                if (isOneTime) {
                    unsubscribe(this)
                }

                block()
            }
        }
    )
}

/**
 * Registers the callback [block] to be called when this [Lifecycle] is stopped.
 *
 * @param isOneTime if `true` then the callback is automatically unregistered right before
 * the first call, default value is `false`.
 */
inline fun Lifecycle.doOnStop(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    subscribe(
        object : Lifecycle.Callbacks {
            override fun onStop() {
                if (isOneTime) {
                    unsubscribe(this)
                }

                block()
            }
        }
    )
}

/**
 * Registers the callback [block] to be called when this [Lifecycle] is destroyed.
 * Calls the [block] immediately if the [Lifecycle] is already destroyed.
 */
inline fun Lifecycle.doOnDestroy(crossinline block: () -> Unit) {
    if (state == Lifecycle.State.DESTROYED) {
        block()
    } else {
        subscribe(
            object : Lifecycle.Callbacks {
                override fun onDestroy() {
                    block()
                }
            }
        )
    }
}

/**
 * Convenience method for [Lifecycle.doOnCreate].
 */
inline fun LifecycleOwner.doOnCreate(crossinline block: () -> Unit) {
    lifecycle.doOnCreate(block)
}

/**
 * Convenience method for [Lifecycle.doOnStart].
 */
inline fun LifecycleOwner.doOnStart(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    lifecycle.doOnStart(isOneTime = isOneTime, block = block)
}

/**
 * Convenience method for [Lifecycle.doOnResume].
 */
inline fun LifecycleOwner.doOnResume(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    lifecycle.doOnResume(isOneTime = isOneTime, block = block)
}

/**
 * Convenience method for [Lifecycle.doOnPause].
 */
inline fun LifecycleOwner.doOnPause(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    lifecycle.doOnPause(isOneTime = isOneTime, block = block)
}

/**
 * Convenience method for [Lifecycle.doOnStop].
 */
inline fun LifecycleOwner.doOnStop(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    lifecycle.doOnStop(isOneTime = isOneTime, block = block)
}

/**
 * Convenience method for [Lifecycle.doOnDestroy].
 */
inline fun LifecycleOwner.doOnDestroy(crossinline block: () -> Unit) {
    lifecycle.doOnDestroy(block)
}
