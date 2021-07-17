package com.arkivanov.essenty.instancekeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get

/**
 * Creates a new instance of [InstanceKeeper] and attaches it to the provided AndroidX [ViewModelStore]
 */
@Suppress("FunctionName") // Factory function
fun InstanceKeeper(viewModelStore: ViewModelStore): InstanceKeeper =
    ViewModelProvider(
        viewModelStore,
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = InstanceKeeperViewModel() as T
        }
    )
        .get<InstanceKeeperViewModel>()
        .instanceKeeperDispatcher

/**
 * Creates a new instance of [InstanceKeeper] and attaches it to the AndroidX [ViewModelStore]
 */
fun ViewModelStoreOwner.instanceKeeper(): InstanceKeeper = InstanceKeeper(viewModelStore)

internal class InstanceKeeperViewModel : ViewModel() {
    val instanceKeeperDispatcher: InstanceKeeperDispatcher = InstanceKeeperDispatcher()

    override fun onCleared() {
        instanceKeeperDispatcher.destroy()
    }
}
