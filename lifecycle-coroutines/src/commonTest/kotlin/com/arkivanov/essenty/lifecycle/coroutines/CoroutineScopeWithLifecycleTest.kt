package com.arkivanov.essenty.lifecycle.coroutines

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class CoroutineScopeWithLifecycleTest {

    @Test
    fun GIVEN_lifecycle_not_destroyed_WHEN_scope_created_THEN_scope_is_active() {
        val lifecycle = LifecycleRegistry()

        val scope = CoroutineScope(StandardTestDispatcher()).withLifecycle(lifecycle)

        assertTrue(scope.isActive)
    }

    @Test
    fun GIVEN_lifecycle_destroyed_WHEN_scope_created_THEN_scope_is_not_active() {
        val lifecycle = LifecycleRegistry()
        lifecycle.create()
        lifecycle.destroy()

        val scope = CoroutineScope(StandardTestDispatcher()).withLifecycle(lifecycle)

        assertFalse(scope.isActive)
    }

    @Test
    fun WHEN_lifecycle_destroyed_THEN_scope_is_not_active() {
        val lifecycle = LifecycleRegistry()
        val scope = CoroutineScope(StandardTestDispatcher()).withLifecycle(lifecycle)
        lifecycle.create()

        lifecycle.destroy()

        assertFalse(scope.isActive)
    }
}
