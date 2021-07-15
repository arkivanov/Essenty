package com.arkivanov.essenty.parcelable

import kotlin.js.JsName

@JsName("parcelableContainer")
@Suppress("FunctionName") // Factory function
actual fun ParcelableContainer(value: Parcelable?): ParcelableContainer =
    SimpleParcelableContainer().apply {
        if (value != null) {
            set(value)
        }
    }
