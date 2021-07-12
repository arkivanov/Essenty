package com.arkivanov.essenty.utils.internal

@InternalEssentyApi
actual fun <T : Any> T.ensureNeverFrozen(): T = this
