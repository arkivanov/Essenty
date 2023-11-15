package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * Repeat invocation [block] every time that passed [state] appears.
 * Work of passed [block] finished when "opposite" [Lifecycle.State] will appear.
 *
 * Note: This function works like a terminal operator and must be called in assembly coroutine.
 */
suspend fun Lifecycle.repeatOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    require(state != Lifecycle.State.INITIALIZED) {
        "repeatOnEssentyLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (this.state == Lifecycle.State.DESTROYED) {
        return
    }

    withCoroutineScope {
        if (this@repeatOnLifecycle.state == Lifecycle.State.DESTROYED) {
            return@withCoroutineScope
        }

        var callback: Lifecycle.Callbacks? = null
        var job: Job? = null
        val mutex = Mutex()

        try {
            suspendCancellableCoroutine { cont ->
                callback = createLifecycleAwareCallback(
                    startState = state,
                    onStateAppear = {
                        job = startJob(mutex, block)
                    },
                    onStateDisappear = {
                        job?.cancel()
                        job = null
                    },
                    onDestroy = {
                        cont.resume(Unit)
                    },
                )

                this@repeatOnLifecycle.subscribe(callback as Lifecycle.Callbacks)
            }
        } finally {
            job?.cancel()
            job = null
            callback?.let {
                this@repeatOnLifecycle.unsubscribe(it)
            }
        }
    }
}

/**
 * Function for executing [block] on the [CoroutineScope] with passed specific [dispatcher].
 */
private suspend fun withCoroutineScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    block: suspend CoroutineScope.() -> Unit
): Unit = coroutineScope {
    withContext(dispatcher) {
        block()
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

    override fun onCreate(): Unit = launchIfState(Lifecycle.State.CREATED)

    override fun onStart(): Unit = launchIfState(Lifecycle.State.STARTED)

    override fun onResume(): Unit = launchIfState(Lifecycle.State.RESUMED)

    override fun onPause(): Unit = closeIfState(Lifecycle.State.RESUMED)

    override fun onStop(): Unit = closeIfState(Lifecycle.State.STARTED)

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

/**
 * Start coroutine with passed [block].
 */
private fun CoroutineScope.startJob(
    mutex: Mutex,
    block: suspend CoroutineScope.() -> Unit
): Job = launch {
    // Mutex will prevent duplication if the same callback called twice.
    mutex.withLock {
        block()
    }
}
