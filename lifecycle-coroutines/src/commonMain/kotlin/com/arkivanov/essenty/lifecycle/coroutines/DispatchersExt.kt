package com.arkivanov.essenty.lifecycle.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlin.concurrent.Volatile

@Volatile
private var isImmediateSupported: Boolean = true

@Suppress("UnusedReceiverParameter")
internal val MainCoroutineDispatcher.immediateOrFallback: MainCoroutineDispatcher
    get() {
        if (isImmediateSupported) {
            try {
                return Dispatchers.Main.immediate
            } catch (ignored: UnsupportedOperationException) {
            } catch (ignored: NotImplementedError) {
            }

            isImmediateSupported = false
        }

        return Dispatchers.Main
    }
