package com.arkivanov.essenty.parcelable

actual fun ParcelableContainer(value: Parcelable?): ParcelableContainer =
    AndroidParcelableContainer().apply {
        if (value != null) {
            set(value)
        }
    }
