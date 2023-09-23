package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.TYPE)
actual annotation class WriteWith<P : Parceler<*>> actual constructor()
