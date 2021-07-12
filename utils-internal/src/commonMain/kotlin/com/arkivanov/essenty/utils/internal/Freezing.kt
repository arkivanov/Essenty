package com.arkivanov.essenty.utils.internal

@InternalEssentyApi
expect fun <T : Any> T.ensureNeverFrozen(): T
