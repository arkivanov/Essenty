package com.arkivanov.essenty.lifecycle.reaktive

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import com.badoo.reaktive.disposable.Disposable
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class DisposableWithLifecycleTest {

    @Test
    fun GIVEN_lifecycle_not_destroyed_WHEN_disposable_created_THEN_disposable_is_not_disposed() {
        val lifecycle = LifecycleRegistry()

        val disposable = Disposable().withLifecycle(lifecycle)

        assertFalse(disposable.isDisposed)
    }

    @Test
    fun GIVEN_lifecycle_destroyed_WHEN_disposable_created_THEN_disposable_is_disposed() {
        val lifecycle = LifecycleRegistry()
        lifecycle.create()
        lifecycle.destroy()

        val scope = Disposable().withLifecycle(lifecycle)

        assertTrue(scope.isDisposed)
    }

    @Test
    fun WHEN_lifecycle_destroyed_THEN_disposable_is_disposed() {
        val lifecycle = LifecycleRegistry()
        val scope = Disposable().withLifecycle(lifecycle)
        lifecycle.create()

        lifecycle.destroy()

        assertTrue(scope.isDisposed)
    }
}
