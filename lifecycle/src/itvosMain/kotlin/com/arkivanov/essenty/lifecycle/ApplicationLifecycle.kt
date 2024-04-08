package com.arkivanov.essenty.lifecycle

import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSNotificationName
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationState
import platform.UIKit.UIApplicationWillEnterForegroundNotification
import platform.UIKit.UIApplicationWillResignActiveNotification
import platform.UIKit.UIApplicationWillTerminateNotification
import platform.darwin.NSObjectProtocol

/**
 * An implementation of [Lifecycle] that follows the [UIApplication] lifecycle notifications.
 *
 * Since this implementation subscribes to [UIApplication] global lifecycle events,
 * the instance and all its registered callbacks (and whatever they capture) will stay in
 * memory until the application is destroyed. It's ok to use it in a global scope like
 * `UIApplicationDelegate`, but it may cause memory leaks when used in a narrower scope like
 * `UIViewController` if it gets destroyed earlier.
 */
class ApplicationLifecycle internal constructor(
    private val platform: Platform,
    private val lifecycle: LifecycleRegistry = LifecycleRegistry(),
) : Lifecycle by lifecycle {

    constructor() : this(platform = DefaultPlatform)

    private val willEnterForegroundObserver = platform.addObserver(UIApplicationWillEnterForegroundNotification) { lifecycle.start() }
    private val didBecomeActiveObserver = platform.addObserver(UIApplicationDidBecomeActiveNotification) { lifecycle.resume() }
    private val willResignActiveObserver = platform.addObserver(UIApplicationWillResignActiveNotification) { lifecycle.pause() }
    private val didEnterBackgroundObserver = platform.addObserver(UIApplicationDidEnterBackgroundNotification) { lifecycle.stop() }
    private val willTerminateObserver = platform.addObserver(UIApplicationWillTerminateNotification) { lifecycle.destroy() }

    init {
        platform.addOperationOnMainQueue {
            if (lifecycle.state == Lifecycle.State.INITIALIZED) {
                when (platform.applicationState) {
                    UIApplicationState.UIApplicationStateActive -> lifecycle.resume()
                    UIApplicationState.UIApplicationStateInactive -> lifecycle.start()
                    UIApplicationState.UIApplicationStateBackground -> lifecycle.create()
                    else -> lifecycle.create()
                }
            }
        }

        doOnDestroy {
            platform.removeObserver(willEnterForegroundObserver)
            platform.removeObserver(didBecomeActiveObserver)
            platform.removeObserver(willResignActiveObserver)
            platform.removeObserver(didEnterBackgroundObserver)
            platform.removeObserver(willTerminateObserver)
        }
    }

    /**
     * Destroys this [ApplicationLifecycle] moving it to [Lifecycle.State.DESTROYED] state.
     * Also unsubscribes from all [UIApplication] lifecycle notifications.
     *
     * If the current state is [Lifecycle.State.INITIALIZED], then the lifecycle is first
     * moved to [Lifecycle.State.CREATED] state and then immediately to [Lifecycle.State.DESTROYED] state.
     */
    fun destroy() {
        if (lifecycle.state == Lifecycle.State.INITIALIZED) {
            lifecycle.create()
        }

        lifecycle.destroy()
    }

    internal interface Platform {
        val applicationState: UIApplicationState

        fun addObserver(name: NSNotificationName, block: (NSNotification?) -> Unit): NSObjectProtocol
        fun removeObserver(observer: NSObjectProtocol)
        fun addOperationOnMainQueue(block: () -> Unit)
    }

    internal object DefaultPlatform : Platform {
        override val applicationState: UIApplicationState get() = UIApplication.sharedApplication.applicationState

        override fun addObserver(name: NSNotificationName, block: (NSNotification?) -> Unit): NSObjectProtocol =
            NSNotificationCenter.defaultCenter.addObserverForName(
                name = name,
                `object` = null,
                queue = NSOperationQueue.mainQueue,
                usingBlock = block,
            )

        override fun removeObserver(observer: NSObjectProtocol) {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }

        override fun addOperationOnMainQueue(block: () -> Unit) {
            NSOperationQueue.mainQueue.addOperationWithBlock(block)
        }
    }
}
