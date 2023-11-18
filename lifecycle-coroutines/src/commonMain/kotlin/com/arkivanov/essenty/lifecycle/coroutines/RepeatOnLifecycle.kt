package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
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
 * Convenience method for [Lifecycle.repeatOnLifecycle].
 */
suspend fun LifecycleOwner.repeatOnLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = Dispatchers.Main.immediateOrFallback,
    block: suspend CoroutineScope.() -> Unit,
) {
    lifecycle.repeatOnLifecycle(minActiveState = minActiveState, context = context, block = block)
}

/**
 * Runs the given [block] in a new coroutine when this [Lifecycle] is at least at [minActiveState] and suspends
 * the execution until this [Lifecycle] is [Lifecycle.State.DESTROYED].
 *
 * The [block] will cancel and re-launch as the [Lifecycle] moves in and out of the [minActiveState].
 *
 * The [block] is called on the specified [context], which defaults to
 * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate]
 * if available on the current platform, or to [Dispatchers.Main] otherwise.
 *
 * See the [AndroidX documentation](https://developer.android.com/reference/kotlin/androidx/lifecycle/package-summary#(androidx.lifecycle.Lifecycle).repeatOnLifecycle(androidx.lifecycle.Lifecycle.State,kotlin.coroutines.SuspendFunction1))
 * for more information.
 */
suspend fun Lifecycle.repeatOnLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = Dispatchers.Main.immediateOrFallback,
    block: suspend CoroutineScope.() -> Unit
) {
    require(minActiveState != Lifecycle.State.INITIALIZED) {
        "repeatOnEssentyLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (this.state == Lifecycle.State.DESTROYED) {
        return
    }

    coroutineScope {
        withContext(context) {
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
