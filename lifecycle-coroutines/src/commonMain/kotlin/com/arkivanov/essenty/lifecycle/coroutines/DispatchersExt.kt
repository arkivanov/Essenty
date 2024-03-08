package com.arkivanov.essenty.lifecycle.coroutines

import kotlinx.coroutines.MainCoroutineDispatcher
import kotlin.concurrent.Volatile

@Volatile
private var isImmediateSupported: Boolean = true

internal val MainCoroutineDispatcher.immediateOrFallback: MainCoroutineDispatcher
    get() {
        if (isImmediateSupported) {
            try {
                return immediate
            } catch (ignored: UnsupportedOperationException) {
            } catch (ignored: NotImplementedError) {
            }

            isImmediateSupported = false
        }

        return this
    }
