package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

/**
 * Repeat invocation [block] every time that passed [minActiveState] appears.
 * Work of passed [block] finished when "opposite" [Lifecycle.State] will appear.
 *
 * Note: This function works like a terminal operator and must be called in assembly coroutine.
 */
suspend fun Lifecycle.repeatOnLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    coroutineContext: CoroutineContext = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
) {
    require(minActiveState != Lifecycle.State.INITIALIZED) {
        "repeatOnEssentyLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (this.state == Lifecycle.State.DESTROYED) {
        return
    }

    coroutineScope {
        withContext(coroutineContext) {
            if (this@repeatOnLifecycle.state == Lifecycle.State.DESTROYED) {
                return@withContext
            }

            var callback: Lifecycle.Callbacks? = null
            var job: Job? = null
            val mutex = Mutex()

            try {
                suspendCancellableCoroutine { cont ->
                    callback = createLifecycleAwareCallback(
                        startState = minActiveState,
                        onStateAppear = {
                            job = launch {
                                mutex.withLock {
                                    block()
                                }
                            }
                        },
                        onStateDisappear = {
                            job?.cancel()
                            job = null
                        },
                        onDestroy = {
                            cont.resume(Unit)
                        },
                    )

                    this@repeatOnLifecycle.subscribe(requireNotNull(callback))
                }
            } finally {
                job?.cancel()
                job = null
                callback?.let {
                    this@repeatOnLifecycle.unsubscribe(it)
                }
                callback = null
            }
        }
    }
}

/**
 * Creates lifecycle aware [Lifecycle.Callbacks] interface instance.
 *
 * @param startState [Lifecycle.State] that [onStateAppear] block must be called from
 * @param onStateAppear block of code that will be executed when the [Lifecycle.State] was equal [startState]
 * @param onStateDisappear block of code that will be executed when the [Lifecycle.State] was equal to opposite [startState]
 * @param onDestroy block of code that will be executed when the [Lifecycle.State] was equal [Lifecycle.State.DESTROYED]
 *
 * @return [Lifecycle.Callbacks]
 */
private fun createLifecycleAwareCallback(
    startState: Lifecycle.State,
    onStateAppear: () -> Unit,
    onStateDisappear: () -> Unit,
    onDestroy: () -> Unit,
): Lifecycle.Callbacks = object : Lifecycle.Callbacks {

    override fun onCreate() {
        launchIfState(Lifecycle.State.CREATED)
    }

    override fun onStart() {
        launchIfState(Lifecycle.State.STARTED)
    }

    override fun onResume() {
        launchIfState(Lifecycle.State.RESUMED)
    }

    override fun onPause() {
        closeIfState(Lifecycle.State.RESUMED)
    }

    override fun onStop() {
        closeIfState(Lifecycle.State.STARTED)
    }

    override fun onDestroy() {
        closeIfState(Lifecycle.State.CREATED)
        onDestroy()
    }

    private fun launchIfState(state: Lifecycle.State) {
        if (startState == state) {
            onStateAppear()
        }
    }

    private fun closeIfState(state: Lifecycle.State) {
        if (startState == state) {
            onStateDisappear()
        }
    }
}
