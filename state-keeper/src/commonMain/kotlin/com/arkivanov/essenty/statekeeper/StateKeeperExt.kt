package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

/**
 * C convenience method for [StateKeeper.consume].
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = PARCELABLE_DEPRECATED_MESSAGE,
    replaceWith = ReplaceWith("this.consume(key = key, strategy = strategy)"),
)
inline fun <reified T : Parcelable> StateKeeper.consume(key: String): T? = consume(key, T::class)
