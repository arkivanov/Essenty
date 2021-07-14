package com.arkivanov.essenty.parcelable

actual fun ParcelableContainer(value: Parcelable?): ParcelableContainer =
    SimpleParcelableContainer().apply {
        if (value != null) {
            set(value)
        }
    }
