package com.arkivanov.essenty.parcelable

@Suppress("FunctionName") // Factory function
actual fun ParcelableContainer(value: Parcelable?): ParcelableContainer =
    SimpleParcelableContainer().apply {
        if (value != null) {
            set(value)
        }
    }
