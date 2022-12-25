package com.arkivanov.essenty.parcelable

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.TYPE)
actual annotation class WriteWith<P : Parceler<*>> actual constructor()
