package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.ParcelableContainer
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.parcelable.consume
import com.arkivanov.essenty.utils.internal.ensureNeverFrozen
import kotlin.reflect.KClass

internal class DefaultStateKeeperDispatcher internal constructor(
    savedState: ParcelableContainer?,
    private val parcelableContainerFactory: (Parcelable) -> ParcelableContainer
) : StateKeeperDispatcher {

    constructor(savedState: ParcelableContainer?) : this(
        savedState = savedState,
        parcelableContainerFactory = { ParcelableContainer(it) } // Lambda because of https://youtrack.jetbrains.com/issue/KT-49186
    )

    init {
        ensureNeverFrozen()
    }

    private val savedState: MutableMap<String, ParcelableContainer>? = savedState?.consume<SavedState>()?.map
    private val suppliers = HashMap<String, () -> Parcelable?>()

    override fun save(): ParcelableContainer {
        val map = HashMap<String, ParcelableContainer>()
        suppliers.forEach { (key, supplier) ->
            supplier()?.also {
                map[key] = parcelableContainerFactory(it)
            }
        }

        return ParcelableContainer(SavedState(map))
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun <T : Parcelable> consume(key: String, clazz: KClass<out T>): T? =
        savedState
            ?.remove(key)
            ?.consume(clazz)

    @Suppress("OVERRIDE_DEPRECATION")
    override fun <T : Parcelable> register(key: String, supplier: () -> T?) {
        check(!isRegistered(key)) { "Another supplier is already registered with the key: $key" }
        suppliers[key] = supplier
    }

    override fun unregister(key: String) {
        check(isRegistered(key)) { "No supplier is registered with the key: $key" }
        suppliers -= key
    }

    override fun isRegistered(key: String): Boolean = key in suppliers

    @Parcelize
    private class SavedState(
        val map: MutableMap<String, ParcelableContainer>
    ) : Parcelable
}
