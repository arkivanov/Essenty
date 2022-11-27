package com.arkivanov.essenty.parcelable

/**
 * A multiplatform alias for `kotlinx.parcelize.IgnoredOnParcel` from the `kotlin-parcelize` Gradle plugin.
 * Make sure you have the `kotlin-parcelize` Gradle plugin applied.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
expect annotation class IgnoredOnParcel
