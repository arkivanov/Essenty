[![Maven Central](https://img.shields.io/maven-central/v/com.arkivanov.essenty/lifecycle?color=blue)](https://search.maven.org/search?q=g:com.arkivanov.essenty)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Twitter URL](https://img.shields.io/badge/Twitter-@arkann1985-blue.svg?style=social&logo=twitter)](https://twitter.com/arkann1985)

# Essenty

The most essential libraries for Kotlin Multiplatform development.

Supported targets:

- `android`
- `jvm`
- `js` (`IR` and `LEGACY`)
- `ios`
- `watchos`
- `tvos`
- `macos`
- `linuxX64`

## Lifecyle

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

## Parcelable and Parcelize

Essenty brings both [Android Parcelable](https://developer.android.com/reference/android/os/Parcelable) interface and the `@Parcelize` annotation from [kotlin-parcelize](https://developer.android.com/kotlin/parcelize) compiler plugin to Kotlin Multiplatform, so they both can be used in common code. This is typically used for state/data preservation over [Android configuration changes](https://developer.android.com/guide/topics/resources/runtime-changes), when writing common code targeting Android.

### Parcelable for Darwin (Apple) targets (experimental)

Additionally, Essenty provides an experimental support of `Parcelable` and `@Parcelize` for all Darwin (Apple) targets via [parcelize-darwin](https://github.com/arkivanov/parcelize-darwin) compiler plugin. This only affects your project's runtime if you explicitly enable the `parcelize-darwin` compiler plugin in your project. Otherwise, it's just no-op.

> ⚠️  If you experience any issues with the `parcelize-darwin` plugin, please report them [here](https://github.com/arkivanov/Essenty/issues).

### Parcelable for JVM targets (experimental)

`Parcelable` interface extends `java.io.Serializable` on JVM. This makes it possible to serialize and deserialize `Parcelable` classes as `ByteArray` using [ObjectOutputStream](https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html) and [ObjectInputStream](https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html).

### Setup

Groovy:
```groovy
plugins {
    id "kotlin-parcelize" // Apply the plugin for Android
    id "com.arkivanov.parcelize.darwin" // Optional, only if you need support for Darwin targets
}

// Add the dependency, typically under the commonMain source set
implementation "com.arkivanov.essenty:parcelable:<essenty_version>"
```

Kotlin:
```kotlin
plugins {
    id("kotlin-parcelize") // Apply the plugin for Android
    id("com.arkivanov.parcelize.darwin") // Optional, only if you need support for Darwin targets
}

// Add the dependency, typically under the commonMain source set
implementation("com.arkivanov.essenty:parcelable:<essenty_version>")
```

The `parcelize-darwin` is published on Maven Central, you may need to add `mavenCentral()` repository to your project. You can find more information about `parcelize-darwin` plugin setup [here](https://github.com/arkivanov/parcelize-darwin).

### Usage example

Once the dependency is added and the plugin is applied, we can use it as follows:

```kotlin
// In commonMain

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class User(
    val id: Long,
    val name: String
) : Parcelable
```

When compiled for Android, the `Parcelable` implementation will be generated automatically. When compiled for other targets, it will be just a regular class without any extra generated code.

### Custom Parcelers

> ⚠️  Supported only on Android and Darwin (Apple) targets. For Darwin (Apple) targets the support was added in version `1.2.0-alpha-06`.

If you don't own the type that you need to `@Parcelize`, you can write a custom `Parceler` for it (similar to [kotlin-parcelize](https://developer.android.com/kotlin/parcelize#custom_parcelers)).

#### Simple option in commonMain

```kotlin
import com.arkivanov.essenty.parcelable.CommonParceler
import com.arkivanov.essenty.parcelable.ParcelReader
import com.arkivanov.essenty.parcelable.ParcelWriter
import com.arkivanov.essenty.parcelable.readLong
import com.arkivanov.essenty.parcelable.writeLong
import kotlinx.datetime.Instant

internal object InstantParceler : CommonParceler<Instant> {
    override fun create(reader: ParcelReader): Instant =
        Instant.fromEpochSeconds(reader.readLong())

    override fun Instant.write(writer: ParcelWriter) {
        writer.writeLong(epochSeconds)
    }
}
```

#### Full-featured parcelers with platform-specific serialization

```kotlin
// In commonMain

import com.arkivanov.essenty.parcelable.Parceler
import kotlinx.datetime.Instant

internal expect object InstantParceler : Parceler<Instant>
```

```kotlin
// In androidMain

import com.arkivanov.essenty.parcelable.Parceler
import kotlinx.datetime.Instant

internal actual object InstantParceler : Parceler<Instant> {
    override fun create(parcel: Parcel): Instant = 
        Instant.fromEpochSeconds(parcel.readLong())

    override fun Instant.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(epochSeconds) 
    }
}
```

```kotlin
// In iosMain or darwinMain

import com.arkivanov.essenty.parcelable.Parceler
import kotlinx.datetime.Instant
import platform.Foundation.NSCoder
import platform.Foundation.decodeInt64ForKey
import platform.Foundation.encodeInt64

internal actual object InstantParceler : Parceler<Instant> {
    override fun create(coder: NSCoder): Instant =
        Instant.fromEpochSeconds(coder.decodeInt64ForKey(key = "epochSeconds"))

    override fun Instant.write(coder: NSCoder) {
        coder.encodeInt64(value = epochSeconds, forKey = "epochSeconds")
    }
}
```

```kotlin
// In all other sources (or in a custom nonAndroidMain source set)

internal actual object InstantParceler : Parceler<Instant>
```

#### Applying the parceler

```kotlin
// In commonMain

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.parcelable.TypeParceler
import com.arkivanov.essenty.parcelable.WriteWith
import kotlinx.datetime.Instant

// Class-local parceler
@Parcelize
@TypeParceler<Instant, InstantParceler>()
data class User(
    val id: Long,
    val name: String,
    val dateOfBirth: Instant,
) : Parcelable

// Type-local parceler
@Parcelize
data class User(
    val id: Long,
    val name: String,
    val dateOfBirth: @WriteWith<InstantParceler> Instant,
) : Parcelable
```

## StateKeeper

When writing common code targeting Android, it might be required to preserve some data over Android configuration changes or process death. For this purpose, Essenty provides the `StateKeeper` API, which is inspired by the AndroidX [SavedStateHandle](https://developer.android.com/reference/androidx/lifecycle/SavedStateHandle).

> ⚠️  The `StateKeeper` API relies on the `Parcelable` interface provided by the `parcelable` module described above. It can fail in non-instrumented Android tests (unit tests). Consider using your own test implementations or mocks.

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

The main [StateKeeper](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeper.kt) interface provides ability to register/unregister state suppliers, and also to consume any previously saved state. You can also find some handy [extension functions](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeperExt.kt).

The [StateKeeperDispatcher](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeperDispatcher.kt) interface extends `StateKeeper` and  allows state saving, by calling all registered state providers.

The [StateKeeperOwner](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/commonMain/kotlin/com/arkivanov/essenty/statekeeper/StateKeeperOwner.kt) interface is just a holder of `StateKeeper`. It may be implemented by an arbitrary class, to provide convenient API.

#### Android extensions

From Android side, `StateKeeper` can be obtained by using special functions, can be found [here](https://github.com/arkivanov/Essenty/blob/master/state-keeper/src/androidMain/kotlin/com/arkivanov/essenty/statekeeper/AndroidExt.kt).

### Usage example

#### Using the StateKeeper

```kotlin
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume

class SomeLogic(stateKeeper: StateKeeper) {
    // Use the saved State if any, otherwise create a new State
    private var state: State = stateKeeper.consume("SAVED_STATE") ?: State()

    init {
        // Register the State supplier
        stateKeeper.register("SAVED_STATE") { state }
    }

    @Parcelize
    private class State(
        val someValue: Int = 0
    ) : Parcelable
}
```

#### Using the StateKeeperDisptacher manually

A default implementation of the `StateKeeperDisptacher` interface can be instantiated using the corresponding builder function:

```kotlin
import com.arkivanov.essenty.parcelable.ParcelableContainer
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher

val stateKeeperDispatcher = StateKeeperDispatcher(/*Previously saved state, or null*/)
val someLogic = SomeLogic(stateKeeperDispatcher)

// At some point later
val savedState: ParcelableContainer = stateKeeperDispatcher.save()
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
    private val thing: RetainedThing = instanceKeeper.getOrCreate { RetainedThing() }
}

/*
 * Survives Android configuration changes.
 * ⚠️ Pay attention to not leak any dependencies.
 */
class RetainedThing : InstanceKeeper.Instance {
    override fun onDestroy() {
        // Called when the screen is finished
    }
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

### Predictive Back Gesture (starting from v1.2.x)

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
