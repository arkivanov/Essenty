package com.arkivanov.essenty.parcelable

expect interface Parceler<T>

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.TYPE)
expect annotation class WriteWith<P : Parceler<*>>()
