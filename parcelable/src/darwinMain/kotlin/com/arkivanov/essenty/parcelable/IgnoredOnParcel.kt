package com.arkivanov.essenty.parcelable

/**
 * A multiplatform alias for platform-specific `IgnoredOnParcel` annotations.
 * Not supported on Darwin (Apple) targets.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
actual annotation class IgnoredOnParcel actual constructor()
