package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleEventCallbacks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * Repeat invocation [block] every time that passed [state] appears.
 * Work of passed [block] finished when "opposite" [Lifecycle.State] will appear.
 */
suspend fun Lifecycle.repeatOnEssentyLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    require(state !== Lifecycle.State.INITIALIZED) {
        "repeatOnEssentyLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (this.state === Lifecycle.State.DESTROYED) return

    coroutineScope {
        withContext(Dispatchers.Main.immediate) {
            if (this@repeatOnEssentyLifecycle.state === Lifecycle.State.DESTROYED) return@withContext

            var launchedJob: Job? = null

            // Registered observer
            var callback: LifecycleEventCallbacks? = null
            try {
                suspendCancellableCoroutine<Unit> { cont ->
                    val startWorkLifecycleEvent = Lifecycle.Event.upTo(state)
                    val cancelWorkLifecycleEvent = Lifecycle.Event.downFrom(state)
                    val mutex = Mutex()

                    callback = object : LifecycleEventCallbacks() {
                        override fun onStateChanged(lifecycleEvent: Lifecycle.Event) {
                            if (lifecycleEvent == startWorkLifecycleEvent) {
                                launchedJob = this@coroutineScope.launch {
                                    mutex.withLock {
                                        coroutineScope {
                                            block()
                                        }
                                    }
                                }
                                return
                            }
                            if (lifecycleEvent == cancelWorkLifecycleEvent) {
                                launchedJob?.cancel()
                                launchedJob = null
                            }
                            if (lifecycleEvent == Lifecycle.Event.ON_DESTROY) {
                                cont.resume(Unit)
                            }
                        }
                    }
                    this@repeatOnEssentyLifecycle.subscribe(callback as LifecycleEventCallbacks)
                }
            } finally {
                launchedJob?.cancel()
                callback?.let {
                    this@repeatOnEssentyLifecycle.unsubscribe(it)
                }
            }
        }
    }
}

/**
 * Start [Flow] collecting when [Lifecycle.State] is at least as [minActiveState].
 * It stopped collecting if "opposite" [Lifecycle.State] will appear.
 */
fun <T> Flow<T>.flowWithEssentyLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = callbackFlow {
    lifecycle.repeatOnEssentyLifecycle(minActiveState) {
        this@flowWithEssentyLifecycle.collect {
            send(it)
        }
    }
    close()
}
