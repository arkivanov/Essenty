package com.arkivanov.essenty.lifecycle

inline fun Lifecycle.subscribe(
    crossinline onCreate: () -> Unit = {},
    crossinline onStart: () -> Unit = {},
    crossinline onResume: () -> Unit = {},
    crossinline onPause: () -> Unit = {},
    crossinline onStop: () -> Unit = {},
    crossinline onDestroy: () -> Unit = {},
): Lifecycle.Callbacks =
    object : Lifecycle.Callbacks {
        override fun onCreate() {
            onCreate.invoke()
        }

        override fun onStart() {
            onStart.invoke()
        }

        override fun onResume() {
            onResume.invoke()
        }

        override fun onPause() {
            onPause.invoke()
        }

        override fun onStop() {
            onStop.invoke()
        }

        override fun onDestroy() {
            onDestroy.invoke()
        }
    }.also(::subscribe)

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

inline fun Lifecycle.doOnDestroy(crossinline block: () -> Unit) {
    subscribe(onDestroy = block)
}
