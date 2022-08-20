package com.arkivanov.essenty.parcelable

import kotlin.reflect.KClass

/**
 * A convenience method for [ParcelableContainer.consume]. Throws [IllegalArgumentException]
 * if the [ParcelableContainer] is empty.
 */
fun <T : Parcelable> ParcelableContainer.consumeRequired(clazz: KClass<out T>): T = requireNotNull(consume(clazz))

/**
 * A convenience method for [ParcelableContainer.consume].
 */
inline fun <reified T : Parcelable> ParcelableContainer.consume(): T? = consume(T::class)

/**
 * A convenience method for [ParcelableContainer.consume]. Throws [IllegalArgumentException]
 * if the [ParcelableContainer] is empty.
 */
inline fun <reified T : Parcelable> ParcelableContainer.consumeRequired(): T = consumeRequired(T::class)
