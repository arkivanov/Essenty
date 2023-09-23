package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
actual annotation class IgnoredOnParcel actual constructor()
