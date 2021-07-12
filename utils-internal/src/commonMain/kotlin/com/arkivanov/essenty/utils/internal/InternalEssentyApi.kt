package com.arkivanov.essenty.utils.internal

@RequiresOptIn(message = "This API is internal, please don't use it.", level = RequiresOptIn.Level.ERROR)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class InternalEssentyApi
