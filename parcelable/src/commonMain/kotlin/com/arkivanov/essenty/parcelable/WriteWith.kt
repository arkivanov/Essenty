package com.arkivanov.essenty.parcelable

/**
 * A multiplatform alias for `kotlinx.parcelize.WriteWith` from the `kotlin-parcelize` Gradle plugin.
 * Make sure you have the `kotlin-parcelize` Gradle plugin applied.
 */
@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.TYPE)
expect annotation class WriteWith<P : Parceler<*>>()
