package com.arkivanov.essenty.parcelable

import kotlin.js.JsName
import kotlin.reflect.KClass

interface ParcelableContainer : Parcelable {

    fun <T : Parcelable> consume(clazz: KClass<out T>): T?

    fun set(value: Parcelable?)
}

@JsName("parcelableContainer")
@Suppress("FunctionName") // Factory function
expect fun ParcelableContainer(value: Parcelable? = null): ParcelableContainer
