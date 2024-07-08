[![Maven Central](https://img.shields.io/maven-central/v/com.arkivanov.essenty/lifecycle?color=blue)](https://search.maven.org/search?q=g:com.arkivanov.essenty)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Twitter URL](https://img.shields.io/badge/Twitter-@arkann1985-blue.svg?style=social&logo=twitter)](https://twitter.com/arkann1985)

# Essenty

The most essential libraries for Kotlin Multiplatform development.

Supported targets:

- `android`
- `jvm`
- `js`
- `wasmJs`
- `ios`
- `watchos`
- `tvos`
- `macos`
- `linuxX64`

## Lifecycle

When writing Kotlin Multiplatform (common) code we often need to handle lifecycle events of a screen. For example, to stop background operations when the screen is destroyed, or to reload some data when the screen is activated. Essenty provides the `Lifecycle` API to help with lifecycle handling in the common code. It is very similar to [Android Activity lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle).

### Setup

Groovy:
```groovy
// Add the dependency, typically under the commonMain source set
implementation "com.arkivanov.essenty:lifecycle:<essenty_version>"
```

Kotlin:
```kotlin
// Add the dependency, typically under the commonMain source set
implementation("com.arkivanov.essenty:lifecycle:<essenty_version>")
```

### Lifecycle state transitions

<img src="docs/media/LifecycleStates.png" width="512">

### Content

The main [Lifecycle](https://github.com/arkivanov/Essenty/blob/master/lifecycle/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/Lifecycle.kt) interface provides ability to observe the lifecycle state changes. There are also handy [extension functions](https://github.com/arkivanov/Essenty/blob/master/lifecycle/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/LifecycleExt.kt) for convenience.

The [LifecycleRegistry](https://github.com/arkivanov/Essenty/blob/master/lifecycle/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/LifecycleRegistry.kt) interface extends both the `Lifecycle` and the `Lifecycle.Callbacks` at the same time. It can be used to manually control the lifecycle, for example in tests. You can also find some useful [extension functions](https://github.com/arkivanov/Essenty/blob/master/lifecycle/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/LifecycleRegistryExt.kt).

The [LifecycleOwner](https://github.com/arkivanov/Essenty/blob/master/lifecycle/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/LifecycleOwner.kt) just holds the `Lifecyle`. It may be implemented by an arbitrary class, to provide convenient API.

#### Android extensions

From Android, the `Lifecycle` can be obtained by using special functions, can be found [here](https://github.com/arkivanov/Essenty/blob/master/lifecycle/src/androidMain/kotlin/com/arkivanov/essenty/lifecycle/AndroidExt.kt).

#### iOS and tvOS extensions

There is [ApplicationLifecycle](https://github.com/arkivanov/Essenty/blob/master/lifecycle/src/itvosMain/kotlin/com/arkivanov/essenty/lifecycle/ApplicationLifecycle.kt) awailable for `ios` and `tvos` targets. It follows the `UIApplication` lifecycle notifications.

> ⚠️  Since this implementation subscribes to `UIApplication` global lifecycle events, the instance and all its registered callbacks (and whatever they capture) will stay in memory until the application is destroyed or until `ApplicationLifecycle#destroy` method is called. It's ok to use it in a global scope like `UIApplicationDelegate`, but it may cause memory leaks when used in a narrower scope like `UIViewController` if it gets destroyed earlier. Use the `destroy` method to destroy the lifecycle manually and prevent memory leaks.

#### Reaktive extensions

There are some useful `Lifecycle` extensions for Reaktive.

- Automatic management of `Disposable` and `DisposableScope` by `Lifecycle`, can be found [here](https://github.com/arkivanov/Essenty/blob/master/lifecycle-reaktive/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/reaktive/DisposableWithLifecycle.kt).

#### Coroutines extensions

There are some useful `Lifecycle` extensions for Coroutines.

- Automatic management of `CoroutineScope` by `Lifecycle`, can be found [here](https://github.com/arkivanov/Essenty/blob/master/lifecycle-coroutines/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/coroutines/CoroutineScopeWithLifecycle.kt)
- `Flow.withLifecycle(Lifecycle): Flow` - can be found [here](https://github.com/arkivanov/Essenty/blob/master/lifecycle-coroutines/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/coroutines/FlowWithLifecycle.kt).
- `Lifecycle.repeatOnLifecycle(block)` - can be found [here](https://github.com/arkivanov/Essenty/blob/master/lifecycle-coroutines/src/commonMain/kotlin/com/arkivanov/essenty/lifecycle/coroutines/RepeatOnLifecycle.kt).

### Usage example

#### Observing the Lifecyle

The lifecycle can be observed using its `subscribe`/`unsubscribe` methods:

```kotlin
import com.arkivanov.essenty.lifecycle.Lifecycle

class SomeLogic(lifecycle: Lifecycle) {
    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    // Handle lifecycle created
                }

                // onStart, onResume, onPause, onStop are also available

                override fun onDestroy() {
                    // Handle lifecycle destroyed
                }
            }
        )
    }
}
```

Or using the extension functions:

```kotlin
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.subscribe

class SomeLogic(lifecycle: Lifecycle) {
    init {
        lifecycle.subscribe(
            onCreate = { /* Handle lifecycle created */ },
            // onStart, onResume, onPause, onStop are also available
            onDestroy = { /* Handle lifecycle destroyed */ }
        )

        lifecycle.doOnCreate {
            // Handle lifecycle created
        }

        // doOnStart, doOnResume, doOnPause, doOnStop are also available

        lifecycle.doOnDestroy {
            // Handle lifecycle destroyed
        }
    }
}
```

#### Using the LifecycleRegistry manually

A default implementation of the `LifecycleRegisty` interface can be instantiated using the corresponding builder function:

```kotlin
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.destroy

val lifecycleRegistry = LifecycleRegistry()
val someLogic = SomeLogic(lifecycleRegistry)

lifecycleRegistry.resume()

// At some point later
lifecycleRegistry.destroy()
```

## StateKeeper

When writing common code targeting Android, it might be required to preserve some data over process death or Android configuration changes. For this purpose, Essenty provides the `StateKeeper` API, which is inspired by the AndroidX [SavedStateHandle](https://developer.android.com/reference/androidx/lifecycle/SavedStateHandle).

### Setup

Groovy:
```groovy
// Add the dependency, typically under the commonMain source set
implementation "com.arkivanov.essenty:state-keeper:<essenty_version>"
```

Kotlin:
```kotlin
// Add the dependency, typically under the commonMain source set
implementation("com.arkivanov.essenty:state-keeper:<essenty_version>")
```

### Content

The main [StateKeeper](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeper.kt) interface provides ability to register/unregister state suppliers, and also to consume any previously saved state. You can also find some handy [extension functions](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeperExt.kt). You can also find some handy [extension functions](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeperExt.kt).

The [StateKeeperDispatcher](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeperDispatcher.kt) interface extends `StateKeeper` and  allows state saving, by calling all registered state providers.

The [StateKeeperOwner](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeperOwner.kt) interface is just a holder of `StateKeeper`. It may be implemented by an arbitrary class, to provide convenient API.

#### Android extensions

From Android side, `StateKeeper` can be obtained by using special functions, can be found [here](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/androidMain/kotlin/com/arkivanov/essenty/statekeeper/AndroidExt.kt).

There are also some handy [extension functions](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/androidMain/kotlin/com/arkivanov/essenty/statekeeper/BundleExt.kt) for serializing/deserializing `KSerializable` objects to/from [Bundle](https://developer.android.com/reference/android/os/Bundle):

- `fun <T : Any> Bundle.putSerializable(key: String?, value: T?, strategy: SerializationStrategy<T>)`
- `fun <T : Any> Bundle.getSerializable(key: String?, strategy: DeserializationStrategy<T>): T?`
- `fun Bundle.putSerializableContainer(key: String?, value: SerializableContainer?)`
- `fun Bundle.getSerializableContainer(key: String?): SerializableContainer?`

### Usage example

#### Using StateKeeper

> ⚠️  Make sure you [setup](https://github.com/Kotlin/kotlinx.serialization#setup) `kotlinx-serialization` properly. 

```kotlin
import com.arkivanov.essenty.statekeeper.StateKeeper
import kotlinx.serialization.Serializable

class SomeLogic(stateKeeper: StateKeeper) {
    // Use the saved State if any, otherwise create a new State
    private var state: State = stateKeeper.consume(key = "SAVED_STATE", strategy = State.serializer()) ?: State()

    init {
        // Register the State supplier
        stateKeeper.register(key = "SAVED_STATE", strategy = State.serializer()) { state }
    }

    @Serializable
    private class State(
        val someValue: Int = 0
    )
}
```

#### Saveable properties (experimental since version `2.2.0-alpha01`)

```kotlin
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.saveable
import kotlinx.serialization.Serializable

class SomeLogic(stateKeeper: StateKeeper) {
    private var state: State by stateKeeper.saveable(serializer = State.serializer(), init = ::State)

    @Serializable
    private class State(val someValue: Int = 0)
}
```

#### Saveable state holders (experimental since version `2.2.0-alpha01`)

```kotlin
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.saveable
import kotlinx.serialization.Serializable

class SomeLogic(stateKeeper: StateKeeper) {
    private val viewModel by stateKeeper.saveable(serializer = State.serializer(), state = ViewModel::state) { savedState ->
        ViewModel(state = savedState ?: State())
    }

    private class ViewModel(var state: State)

    @Serializable
    private class State(val someValue: Int = 0)
}
```

##### Polymorphic serialization (experimental)

Sometimes it might be necessary to serialize an interface or an abstract class that you don't own but have implemented. For this purpose Essenty provides `polymorphicSerializer` function that can be used to create custom polymorphic serializers for unowned base types.

For example a third-party library may have the following interface.

```kotlin
interface Filter {
    // Omitted code
}
```

Then we can have multiple implementations of `Filter`.

```kotlin
@Serializable
class TextFilter(val text: String) : Filter { /* Omitted code */ }

@Serializable
class RatingFilter(val stars: Int) : Filter { /* Omitted code */ }
```

Now we can create a polymorphic serializer for `Filter` as follows. It can be used to save and restore `Filter` directly via StateKeeper, or to have `Filter` as part of another `Serializable` class.

```kotlin
object FilterSerializer : KSerializer<Filter> by polymorphicSerializer(
    SerializersModule {
        polymorphic(Filter::class) {
            subclass(TextFilter::class, TextFilter.serializer())
            subclass(RatingFilter::class, RatingFilter.serializer())
        }
    }
)
```

#### Using the StateKeeperDispatcher manually

On Android, the `StateKeeper` obtained via one of the extensions described above automatically saves and restores the state. On other platforms (if needed) the state can be saved and restored manually. A default implementation of `StateKeeperDisptacher` interface can be instantiated using the corresponding builder function. The state can be encoded as a JSON string and saved using the corresponding platform-specific API.

```kotlin
import com.arkivanov.essenty.statekeeper.SerializableContainer
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher

val stateKeeperDispatcher = StateKeeperDispatcher(/*Previously saved state, or null*/)
val someLogic = SomeLogic(stateKeeperDispatcher)

// At some point later when it's time to save the state
val savedState: SerializableContainer = stateKeeperDispatcher.save()

// The returned SerializableContainer can now be saved using the corresponding platform-specific API
```

## InstanceKeeper

When writing common code targetting Android, it might be required to retain objects over Android configuration changes. This use case is covered by the `InstanceKeeper` API, which is similar to the AndroidX [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel).

### Setup

Groovy:
```groovy
// Add the dependency, typically under the commonMain source set
implementation "com.arkivanov.essenty:instance-keeper:<essenty_version>"
```

Kotlin:
```kotlin
// Add the dependency, typically under the commonMain source set
implementation("com.arkivanov.essenty:instance-keeper:<essenty_version>")
```

### Content

The main [InstanceKeeper](https://github.com/arkivanov/Essenty/blob/master/instance-keeper/src/commonMain/kotlin/com/arkivanov/essenty/instancekeeper/InstanceKeeper.kt) interface is responsible for storing object instances, represented by the [InstanceKeeper.Instance] interface. Instances of the `InstanceKeeper.Instance` interface survive Android Configuration changes, the `InstanceKeeper.Instance.onDestroy()` method is called when `InstanceKeeper` goes out of scope (e.g. the screen is finished). You can also find some handy [extension functions](https://github.com/arkivanov/Essenty/blob/master/instance-keeper/src/commonMain/kotlin/com/arkivanov/essenty/instancekeeper/InstanceKeeperExt.kt).

The [InstanceKeeperDispatcher](https://github.com/arkivanov/Essenty/blob/master/instance-keeper/src/commonMain/kotlin/com/arkivanov/essenty/instancekeeper/InstanceKeeperDispatcher.kt) interface extends `InstanceKeeper` and adds ability to destroy all registered instances.

The [InstanceKeeperOwner](https://github.com/arkivanov/Essenty/blob/master/instance-keeper/src/commonMain/kotlin/com/arkivanov/essenty/instancekeeper/InstanceKeeperOwner.kt) interface is just a holder of `InstanceKeeper`. It may be implemented by an arbitrary class, to provide convenient API.

#### Android extensions

From Android side, `InstanceKeeper` can be obtained by using special functions, can be found [here](https://github.com/arkivanov/Essenty/blob/master/instance-keeper/src/androidMain/kotlin/com/arkivanov/essenty/instancekeeper/AndroidExt.kt).

### Usage example

#### Using the InstanceKeeper

```kotlin
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate

class SomeLogic(instanceKeeper: InstanceKeeper) {
    // Get the existing instance or create a new one
    private val viewModel = instanceKeeper.getOrCreate { ViewModel() }
}

/*
 * Survives Android configuration changes.
 * ⚠️ Pay attention to not leak any dependencies.
 */
class ViewModel : InstanceKeeper.Instance {
    override fun onDestroy() {
        // Called when the screen is finished
    }
}
```

##### Alternative way (experimental since version 2.2.0-alpha01)

```kotlin
class SomeLogic(instanceKeeperOwner: InstanceKeeperOwner) : InstanceKeeperOwner by instanceKeeperOwner {
    // Get the existing instance or create a new one
    private val viewModel = retainedInstance { ViewModel() }
}
```

#### Using the InstanceKeeperDispatcher manually

A default implementation of the `InstanceKeeperDispatcher` interface can be instantiated using the corresponding builder function:

```kotlin
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher

// Create a new instance of InstanceKeeperDispatcher, or reuse an existing one
val instanceKeeperDispatcher = InstanceKeeperDispatcher()
val someLogic = SomeLogic(instanceKeeperDispatcher)

// At some point later
instanceKeeperDispatcher.destroy()
```

## BackHandler

The `BackHandler` API provides ability to handle back button clicks (e.g. the Android device's back button), in common code. This API is similar to AndroidX [OnBackPressedDispatcher](https://developer.android.com/reference/androidx/activity/OnBackPressedDispatcher).

### Setup

Groovy:
```groovy
// Add the dependency, typically under the commonMain source set
implementation "com.arkivanov.essenty:back-handler:<essenty_version>"
```

Kotlin:
```kotlin
// Add the dependency, typically under the commonMain source set
implementation("com.arkivanov.essenty:back-handler:<essenty_version>")
```

### Content

The [BackHandler](https://github.com/arkivanov/Essenty/blob/master/back-handler/src/commonMain/kotlin/com/arkivanov/essenty/backhandler/BackHandler.kt) interface provides ability to register and unregister back button callbacks. When the device's back button is pressed, all registered callbacks are called in reverse order, the first enabled callback is called and the iteration finishes.

> Starting from `v1.2.x`, when the device's back button is pressed, all registered callbacks are sorted in ascending order first by priority and then by index, the last enabled callback is called.

[BackCallback](https://github.com/arkivanov/Essenty/blob/master/back-handler/src/commonMain/kotlin/com/arkivanov/essenty/backhandler/BackCallback.kt) allows handling back events, including predictive back gestures.

The [BackDispatcher](https://github.com/arkivanov/Essenty/blob/master/back-handler/src/commonMain/kotlin/com/arkivanov/essenty/backhandler/BackDispatcher.kt) interface extends `BackHandler` and is responsible for triggering the registered callbacks. The `BackDispatcher.back()` method triggers all registered callbacks in reverse order, and returns `true` if an enabled callback was called, and `false` if no enabled callback was found.

#### Android extensions

From Android side, `BackHandler` can be obtained by using special functions, can be found [here](https://github.com/arkivanov/Essenty/blob/master/back-handler/src/androidMain/kotlin/com/arkivanov/essenty/backhandler/AndroidBackHandler.kt).

### Predictive Back Gesture

Both `BackHandler` and `BackDispatcher` bring the new [Android Predictive Back Gesture](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture) to Kotlin Multiplatform. 

#### Predictive Back Gesture on Android

On Android, the predictive back gesture only works starting with Android T. On Android T, it works only between Activities, if enabled in the system settings. Starting with Android U, the predictive back gesture also works between application's screens inside an Activity. In the latter case, back gesture events can be handled using `BackCallback`.

#### Predictive Back Gesture on other platforms

On all other platforms, predictive back gestures can be dispatched manually via `BackDispatcher`. This can be done e.g. by adding an overlay on top of the UI and handling touch events manually.

### Usage example

#### Using the BackHandler

```kotlin
import com.arkivanov.essenty.backhandler.BackHandler

class SomeLogic(backHandler: BackHandler) {
    private val callback = BackCallback {
        // Called when the back button is pressed
    }

    init {
        backHandler.register(callback)

        // Disable the callback when needed
        callback.isEnabled = false
    }
}
```

#### Using the BackDispatcher manually

A default implementation of the `BackDispatcher` interface can be instantiated using the corresponding builder function:

```kotlin
import com.arkivanov.essenty.backhandler.BackDispatcher

val backDispatcher = BackDispatcher()
val someLogic = SomeLogic(backDispatcher)

if (!backDispatcher.back()) {
    // The back pressed event was not handled
}
```

## Author

Twitter: [@arkann1985](https://twitter.com/arkann1985)

If you like this project you can always <a href="https://www.buymeacoffee.com/arkivanov" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" alt="Buy Me A Coffee" height=32></a> ;-)
