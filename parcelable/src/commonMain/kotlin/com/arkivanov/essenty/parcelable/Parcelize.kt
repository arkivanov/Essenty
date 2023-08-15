package com.arkivanov.essenty.parcelable

/**
 * A multiplatform alias for platform-specific `Parcelize` annotations.
 *
 * On Android, refers to `kotlinx.parcelize.Parcelize` from the `kotlin-parcelize` Gradle plugin.
 *
 * On Darwin (Apple) targets, refers to `com.arkivanov.parcelize.darwin.Parcelize` from
 * the `parcelize-darwin` Gradle plugin.
 *
 * Make sure you have the required Gradle plugins applied if you need `Parcelize` support.
 * The `parcelize-darwin` plugin is optional, only apply if you actually need serialization.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize()
