package com.arkivanov.essenty.parcelable

/**
 * Represents an object that can be serialized and deserialized.
 *
 * Currently the actual serialization is only supported on Android.
 * It's a no-op on all other platforms.
 * See [https://github.com/arkivanov/Essenty/discussions/43] for details.
 */
expect interface Parcelable
