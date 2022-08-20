package com.arkivanov.essenty.parcelable

import kotlin.js.JsName
import kotlin.reflect.KClass

/**
 * Represents a container for a [Parcelable] object.
 */
interface ParcelableContainer : Parcelable {

    /**
     * Clears and returns a previously stored [Parcelable] object.
     *
     * @param clazz a class of the [Parcelable] object, used for deserialization.
     */
    fun <T : Parcelable> consume(clazz: KClass<out T>): T?

    /**
     * Stores a [Parcelable] object, clearing any previously stored object.
     */
    fun set(value: Parcelable?)
}

/**
 * Creates [ParcelableContainer] with the given [value].
 */
@JsName("parcelableContainer")
@Suppress("FunctionName") // Factory function
expect fun ParcelableContainer(value: Parcelable? = null): ParcelableContainer
