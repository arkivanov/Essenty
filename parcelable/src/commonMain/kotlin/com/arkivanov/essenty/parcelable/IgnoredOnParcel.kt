package com.arkivanov.essenty.parcelable

/**
 * A multiplatform alias for platform-specific `IgnoredOnParcel` annotations.
 *
 * On Android, refers to `kotlinx.parcelize.IgnoredOnParcel` from the `kotlin-parcelize` Gradle plugin.
 *
 * Not supported on Darwin (Apple) targets.
 *
 * No-op on all other targets.
 *
 * Make sure you have the required Gradle plugins applied if you need `Parcelize` support.
 * The `parcelize-darwin` plugin is optional, only apply if you actually need serialization.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
expect annotation class IgnoredOnParcel()
