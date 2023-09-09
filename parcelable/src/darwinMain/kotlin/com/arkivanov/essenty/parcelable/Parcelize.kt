package com.arkivanov.essenty.parcelable

import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE

/**
 * A multiplatform alias for `com.arkivanov.parcelize.darwin.Parcelize` from the `parcelize-darwin` Gradle plugin.
 * Make sure you have the `parcelize-darwin` Gradle plugin applied.
 */
@Deprecated(PARCELABLE_DEPRECATED_MESSAGE)
actual typealias Parcelize = com.arkivanov.parcelize.darwin.Parcelize
