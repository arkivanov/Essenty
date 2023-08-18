package com.arkivanov.essenty.parcelable

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ParcelizeTest : AbstractParcelizeTest() {

    override fun serializeAndDeserialize(data: Data): Data =
        data.serializeAndDeserialize()
}
