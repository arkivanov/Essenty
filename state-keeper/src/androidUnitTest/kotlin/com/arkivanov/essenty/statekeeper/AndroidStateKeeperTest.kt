package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import android.os.Parcel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.serialization.builtins.serializer
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("TestFunctionName")
@RunWith(RobolectricTestRunner::class)
class AndroidStateKeeperTest {

    @Test
    fun saves_and_restores_state_without_parcelling() {
        var savedStateRegistryOwner = TestSavedStateRegistryOwner()
        savedStateRegistryOwner.controller.performRestore(null)
        var stateKeeper = savedStateRegistryOwner.stateKeeper()
        stateKeeper.register(key = "key", strategy = String.serializer()) { "data" }
        val bundle = Bundle()
        savedStateRegistryOwner.controller.performSave(bundle)

        savedStateRegistryOwner = TestSavedStateRegistryOwner()
        savedStateRegistryOwner.controller.performRestore(bundle)
        stateKeeper = StateKeeper(savedStateRegistry = savedStateRegistryOwner.savedStateRegistry)
        val restoredData = stateKeeper.consume(key = "key", strategy = String.serializer())

        assertEquals("data", restoredData)
    }

    @Test
    fun saves_and_restores_state_with_parcelling() {
        var savedStateRegistryOwner = TestSavedStateRegistryOwner()
        savedStateRegistryOwner.controller.performRestore(null)
        var stateKeeper = savedStateRegistryOwner.stateKeeper()
        stateKeeper.register(key = "key", strategy = String.serializer()) { "data" }
        val bundle = Bundle()
        savedStateRegistryOwner.controller.performSave(bundle)

        savedStateRegistryOwner = TestSavedStateRegistryOwner()
        savedStateRegistryOwner.controller.performRestore(bundle.parcelize().deparcelize())
        stateKeeper = StateKeeper(savedStateRegistry = savedStateRegistryOwner.savedStateRegistry)
        val restoredData = stateKeeper.consume(key = "key", strategy = String.serializer())

        assertEquals("data", restoredData)
    }

    @Test
    fun GIVEN_isSavingAllowed_is_false_on_save_THEN_state_not_saved() {
        val savedStateRegistryOwner = TestSavedStateRegistryOwner()
        savedStateRegistryOwner.controller.performRestore(null)
        val stateKeeper = savedStateRegistryOwner.stateKeeper(isSavingAllowed = { false })
        stateKeeper.register(key = "key", strategy = String.serializer()) { throw IllegalStateException("Must not be called") }
        val bundle = Bundle()

        savedStateRegistryOwner.controller.performSave(bundle)
    }

    @Test
    fun GIVEN_discardSavedState_is_true_on_restore_THEN_discards_saved_state() {
        var savedStateRegistryOwner = TestSavedStateRegistryOwner()
        savedStateRegistryOwner.controller.performRestore(null)
        var stateKeeper = savedStateRegistryOwner.stateKeeper()
        stateKeeper.register(key = "key", strategy = String.serializer()) { "data" }
        val bundle = Bundle()
        savedStateRegistryOwner.controller.performSave(bundle)

        savedStateRegistryOwner = TestSavedStateRegistryOwner()
        savedStateRegistryOwner.controller.performRestore(bundle.parcelize().deparcelize())
        stateKeeper = savedStateRegistryOwner.stateKeeper(discardSavedState = true)
        val restoredData = stateKeeper.consume(key = "key", strategy = String.serializer())

        assertNull(restoredData)
    }

    private fun Bundle.parcelize(): ByteArray {
        val parcel = Parcel.obtain()
        parcel.writeBundle(this)
        return parcel.marshall()
    }

    private fun ByteArray.deparcelize(): Bundle {
        val parcel = Parcel.obtain()
        parcel.unmarshall(this, 0, size)
        parcel.setDataPosition(0)

        return requireNotNull(parcel.readBundle())
    }

    private class TestSavedStateRegistryOwner : SavedStateRegistryOwner {
        val controller: SavedStateRegistryController = SavedStateRegistryController.create(this)

        override val lifecycle: Lifecycle = LifecycleRegistry(this)
        override val savedStateRegistry: SavedStateRegistry get() = controller.savedStateRegistry
    }
}
