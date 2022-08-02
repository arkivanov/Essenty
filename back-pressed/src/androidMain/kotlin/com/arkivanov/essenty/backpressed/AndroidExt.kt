package com.arkivanov.essenty.backpressed

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner

/**
 * Creates a new instance of [BackPressedHandler] and attaches it to the provided AndroidX [OnBackPressedDispatcher].
 *
 * **Caution**
 *
 * Due to the nature of the [OnBackPressedDispatcher] API, it is not possible to map it 1-1 to
 * [BackPressedHandler]. The [OnBackPressedCallback] registered by the [BackPressedHandler] is always `enabled`.
 * When [OnBackPressedDispatcher] calls the registered [OnBackPressedCallback], [BackPressedHandler] in turn calls
 * all its registered callbacks. If the event is *not* consumed (all callbacks returned `false`), then
 * [BackPressedHandler] will `disable` its [OnBackPressedCallback], call [OnBackPressedDispatcher.onBackPressed]
 * recursively, and `enable` the [OnBackPressedCallback] after the call returns. This allows
 * [OnBackPressedDispatcher] to call its fallback callback, and so the `Activity` is closed.
 *
 * This has one important side effect, if there are other [OnBackPressedCallback]s registered in
 * [OnBackPressedDispatcher] (not only [BackPressedHandler]). When [BackPressedHandler] consumes the event and
 * calls [OnBackPressedDispatcher.onBackPressed] recursively, all previously called [OnBackPressedCallback]s are
 * called again.
 *
 * This typically won't cause any negative effect, especially if [BackPressedHandler] is the only
 * [OnBackPressedCallback] registered. However if the side effect described above is undesired, then consider
 * creating and calling [BackPressedDispatcher] manually.
 *
 * Example of using `BackPressedDispatcher` manually:
 *
 * ```
 * class SomeActivity : AppCompatActivity() {
 *     private val backPressedDispatcher = BackPressedDispatcher()
 *
 *     override fun onBackPressed() {
 *         if (!backPressedDispatcher.onBackPressed()) {
 *             super.onBackPressed()
 *         }
 *     }
 * }
 * ```
 */
@Deprecated(
    "Use BackHandler from back-handler module.",
    ReplaceWith("BackHandler(onBackPressedDispatcher)", "com.arkivanov.essenty.backhandler.BackHandler"),
)
@Suppress("FunctionName") // Factory function
fun BackPressedHandler(onBackPressedDispatcher: OnBackPressedDispatcher): BackPressedHandler =
    DelegatedBackPressedDispatcher(onBackPressedDispatcher)

/**
 * Creates a new instance of [BackPressedHandler] and attaches it to the AndroidX [OnBackPressedDispatcher].
 *
 * **Caution**
 *
 * Due to the nature of the [OnBackPressedDispatcher] API, it is not possible to map it 1-1 to
 * [BackPressedHandler]. The [OnBackPressedCallback] registered by the [BackPressedHandler] is always `enabled`.
 * When [OnBackPressedDispatcher] calls the registered [OnBackPressedCallback], [BackPressedHandler] in turn calls
 * all its registered callbacks. If the event is *not* consumed (all callbacks returned `false`), then
 * [BackPressedHandler] will `disable` its [OnBackPressedCallback], call [OnBackPressedDispatcher.onBackPressed]
 * recursively, and `enable` the [OnBackPressedCallback] after the call returns. This allows
 * [OnBackPressedDispatcher] to call its fallback callback, and so the `Activity` is closed.
 *
 * This has one important side effect, if there are other [OnBackPressedCallback]s registered in
 * [OnBackPressedDispatcher] (not only [BackPressedHandler]). When [BackPressedHandler] consumes the event and
 * calls [OnBackPressedDispatcher.onBackPressed] recursively, all previously called [OnBackPressedCallback]s are
 * called again.
 *
 * This typically won't cause any negative effect, especially if [BackPressedHandler] is the only
 * [OnBackPressedCallback] registered. However if the side effect described above is undesired, then consider
 * creating and calling [BackPressedDispatcher] manually.
 *
 * Example of using `BackPressedDispatcher` manually:
 *
 * ```
 * class SomeActivity : AppCompatActivity() {
 *     private val backPressedDispatcher = BackPressedDispatcher()
 *
 *     override fun onBackPressed() {
 *         if (!backPressedDispatcher.onBackPressed()) {
 *             super.onBackPressed()
 *         }
 *     }
 * }
 * ```
 */
@Deprecated(
    "Use backHandler from back-handler module.",
    ReplaceWith("this.backHandler()", "com.arkivanov.essenty.backhandler.backHandler"),
)
fun OnBackPressedDispatcherOwner.backPressedHandler(): BackPressedHandler =
    DelegatedBackPressedDispatcher(onBackPressedDispatcher)

private class DelegatedBackPressedDispatcher(
    private val delegate: OnBackPressedDispatcher
) : BackPressedDispatcher {

    private var isProcessingDelegateBackPressed = false
    private var handlers = emptySet<() -> Boolean>()

    private val callback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isProcessingDelegateBackPressed = true
                if (!handleBackPressed()) {
                    isEnabled = false
                    delegate.onBackPressed()
                    isEnabled = true
                }
                isProcessingDelegateBackPressed = false
            }

            private fun handleBackPressed(): Boolean =
                handlers.reversed().any { it() }
        }

    override fun register(handler: () -> Boolean) {
        if (handlers.isEmpty()) {
            delegate.addCallback(callback)
        }

        this.handlers += handler
    }

    override fun unregister(handler: () -> Boolean) {
        this.handlers -= handler

        if (handlers.isEmpty()) {
            callback.remove()
        }
    }

    override fun onBackPressed(): Boolean {
        if (!isProcessingDelegateBackPressed) {
            delegate.onBackPressed()
        }

        return true
    }
}
