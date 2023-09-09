package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
actual annotation class TypeParceler<T, P : Parceler<in T>> actual constructor()
