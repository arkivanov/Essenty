package com.arkivanov.essenty.parcelable

@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
actual annotation class TypeParceler<T, P : Parceler<in T>> actual constructor()
