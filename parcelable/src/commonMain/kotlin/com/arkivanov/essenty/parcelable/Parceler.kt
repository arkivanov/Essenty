package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

/**
 * A multiplatform alias for platform-specific `Parceler` interfaces.
 *
 * On Android, refers to `kotlinx.parcelize.Parceler` from the `kotlin-parcelize` Gradle plugin.
 *
 * On Darwin (Apple) targets, refers to `com.arkivanov.parcelize.darwin.Parceler` from
 * the `parcelize-darwin` Gradle plugin.
 *
 * No-op on all other targets.
 *
 * Make sure you have the required Gradle plugins applied if you need `Parcelize` support.
 * The `parcelize-darwin` plugin is optional, only apply if you actually need serialization.
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
expect interface Parceler<T>
