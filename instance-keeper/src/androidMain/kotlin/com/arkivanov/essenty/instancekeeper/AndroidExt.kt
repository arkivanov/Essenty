package com.arkivanov.essenty.instancekeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get

/**
 * Creates a new instance of [InstanceKeeper] and attaches it to the provided AndroidX [ViewModelStore].
 *
 * @param discardRetainedInstances a flag indicating whether any previously retained instances should be
 * discarded and destroyed or not, default value is `false`.
 */
fun InstanceKeeper(
    viewModelStore: ViewModelStore,
    discardRetainedInstances: Boolean = false,
): InstanceKeeper =
    ViewModelProvider(
        viewModelStore,
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = InstanceKeeperViewModel() as T
        }
    )
        .get<InstanceKeeperViewModel>()
        .apply {
            if (discardRetainedInstances) {
                recreate()
            }
        }
        .instanceKeeperDispatcher

/**
 * Creates a new instance of [InstanceKeeper] and attaches it to the AndroidX [ViewModelStore].
 *
 * @param discardRetainedInstances a flag indicating whether any previously retained instances should be
 * discarded and destroyed or not, default value is `false`.
 */
fun ViewModelStoreOwner.instanceKeeper(discardRetainedInstances: Boolean = false): InstanceKeeper =
    InstanceKeeper(viewModelStore = viewModelStore, discardRetainedInstances = discardRetainedInstances)

internal class InstanceKeeperViewModel : ViewModel() {
    var instanceKeeperDispatcher: InstanceKeeperDispatcher = InstanceKeeperDispatcher()
        private set

    override fun onCleared() {
        instanceKeeperDispatcher.destroy()
    }

    fun recreate() {
        instanceKeeperDispatcher.destroy()
        instanceKeeperDispatcher = InstanceKeeperDispatcher()
    }
}
