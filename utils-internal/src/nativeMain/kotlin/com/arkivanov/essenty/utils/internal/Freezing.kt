package com.arkivanov.essenty.utils.internal

import kotlin.native.concurrent.ensureNeverFrozen as ensureNeverFrozenNative

@InternalEssentyApi
actual fun <T : Any> T.ensureNeverFrozen(): T {
    ensureNeverFrozenNative()

    return this
}
