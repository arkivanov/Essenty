package com.arkivanov.essenty.parcelable

@Suppress("FunctionName") // Factory function
actual fun ParcelableContainer(value: Parcelable?): ParcelableContainer =
    DarwinParcelableContainer(value)
