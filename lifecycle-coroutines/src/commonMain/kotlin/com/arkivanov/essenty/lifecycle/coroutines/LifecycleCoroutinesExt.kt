package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineDispatcher
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
 *
 * Note: This function works like a terminal operator and must be called in assembly coroutine.
 */
suspend fun Lifecycle.repeatOnEssentyLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    require(state != Lifecycle.State.INITIALIZED) {
        "repeatOnEssentyLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (this.state == Lifecycle.State.DESTROYED) return

    withCoroutineScope {
        if (this@repeatOnEssentyLifecycle.state == Lifecycle.State.DESTROYED) return@withCoroutineScope

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
                    onDestroy = { cont.resume(Unit) },
                )

                this@repeatOnEssentyLifecycle.subscribe(callback as Lifecycle.Callbacks)
            }
        } finally {
            job?.cancel()
            job = null
            callback?.let {
                this@repeatOnEssentyLifecycle.unsubscribe(it)
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

    override fun onCreate(): Unit = launchWithState(Lifecycle.State.CREATED)

    override fun onStart(): Unit = launchWithState(Lifecycle.State.STARTED)

    override fun onResume(): Unit = launchWithState(Lifecycle.State.RESUMED)

    override fun onPause(): Unit = closeWithState(Lifecycle.State.RESUMED)

    override fun onStop(): Unit = closeWithState(Lifecycle.State.STARTED)

    override fun onDestroy() {
        closeWithState(Lifecycle.State.CREATED)
        onDestroy()
    }

    private fun launchWithState(launchState: Lifecycle.State) {
        if (startState == launchState) onStateAppear()
    }

    private fun closeWithState(launchState: Lifecycle.State) {
        if (startState == launchState) onStateDisappear()
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
