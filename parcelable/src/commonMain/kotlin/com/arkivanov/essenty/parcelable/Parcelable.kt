package com.arkivanov.essenty.parcelable

/**
 * A multiplatform alias for platform-specific `Parcelable` interfaces.
 *
 * On Android, refers to `kotlinx.parcelize.Parcelable` from the `kotlin-parcelize` Gradle plugin.
 *
 * On Darwin (Apple) targets, refers to `com.arkivanov.parcelize.darwin.Parcelable` from
 * the `parcelize-darwin` Gradle plugin.
 *
 * No-op on all other targets.
 *
 * Make sure you have the required Gradle plugins applied if you need `Parcelize` support.
 * The `parcelize-darwin` plugin is optional, only apply if you actually need serialization.
 */
expect interface Parcelable
