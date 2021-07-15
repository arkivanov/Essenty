package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable

inline fun <reified T : Parcelable> StateKeeper.consume(key: String): T? = consume(key, T::class)
