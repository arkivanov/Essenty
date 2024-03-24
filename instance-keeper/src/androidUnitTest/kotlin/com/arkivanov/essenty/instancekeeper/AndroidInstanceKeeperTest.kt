package com.arkivanov.essenty.instancekeeper

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.test.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class AndroidInstanceKeeperTest {

    @Test
    fun retains_instances() {
        val owner = TestOwner()
        var instanceKeeper = owner.instanceKeeper()
        val instance1 = instanceKeeper.provide(key = "key", factory = ::TestInstance)

        instanceKeeper = owner.instanceKeeper()
        val instance2 = instanceKeeper.provide(key = "key", factory = ::TestInstance)

        assertSame(instance1, instance2)
    }

    @Test
    fun GIVEN_discardRetainedInstances_is_true_on_restore_THEN_instances_not_retained() {
        val owner = TestOwner()
        var instanceKeeper = owner.instanceKeeper()
        val instance1 = instanceKeeper.provide(key = "key", factory = ::TestInstance)

        instanceKeeper = owner.instanceKeeper(discardRetainedInstances = true)
        val instance2 = instanceKeeper.provide(key = "key", factory = ::TestInstance)

        assertNotSame(instance1, instance2)
    }

    @Test
    fun GIVEN_discardRetainedInstances_is_true_on_restore_THEN_old_instances_destroyed() {
        val owner = TestOwner()
        val instanceKeeper = owner.instanceKeeper()
        val instance1 = instanceKeeper.provide(key = "key", factory = ::TestInstance)

        owner.instanceKeeper(discardRetainedInstances = true)

        assertTrue(instance1.isDestroyed)
    }

    private class TestOwner : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore = ViewModelStore()
    }

    private class TestInstance : InstanceKeeper.Instance {
        var isDestroyed: Boolean = false

        override fun onDestroy() {
            isDestroyed = true
        }
    }
}
