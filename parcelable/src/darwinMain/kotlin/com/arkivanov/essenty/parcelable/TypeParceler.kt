package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
actual typealias TypeParceler<T, P> = com.arkivanov.parcelize.darwin.TypeParceler<T, P>
