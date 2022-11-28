package com.arkivanov.essenty.parcelable

/**
 * A multiplatform alias for `kotlinx.parcelize.TypeParceler` from the `kotlin-parcelize` Gradle plugin.
 * Make sure you have the `kotlin-parcelize` Gradle plugin applied.
 */
@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
expect annotation class TypeParceler<T, P : Parceler<in T>>()
