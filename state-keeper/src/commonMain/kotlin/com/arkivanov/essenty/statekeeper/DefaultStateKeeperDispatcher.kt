package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.ParcelableContainer
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.parcelable.consume
import com.arkivanov.essenty.utils.internal.ensureNeverFrozen
import kotlin.reflect.KClass

internal class DefaultStateKeeperDispatcher internal constructor(
    savedState: ParcelableContainer?,
    private val parcelableContainerFactory: (Parcelable?) -> ParcelableContainer
) : StateKeeperDispatcher {

    constructor(savedState: ParcelableContainer?) : this(
        savedState = savedState,
        parcelableContainerFactory = { ParcelableContainer(it) } // Lambda because of https://youtrack.jetbrains.com/issue/KT-49186
    )

    init {
        ensureNeverFrozen()
    }

    private val savedState: MutableMap<String, ParcelableContainer>? = savedState?.consume<SavedState>()?.map
    private val suppliers = HashMap<String, () -> Parcelable>()

    override fun save(): ParcelableContainer =
        parcelableContainerFactory(
            SavedState(
                suppliers.mapValuesTo(HashMap()) { (_, supplier) ->
                    parcelableContainerFactory(supplier())
                }
            )
        )

    override fun <T : Parcelable> consume(key: String, clazz: KClass<out T>): T? =
        savedState
            ?.remove(key)
            ?.consume(clazz)

    override fun <T : Parcelable> register(key: String, supplier: () -> T) {
        check(key !in suppliers)
        suppliers[key] = supplier
    }

    override fun unregister(key: String) {
        check(key in suppliers)
        suppliers -= key
    }

    @Parcelize
    private class SavedState(
        val map: HashMap<String, ParcelableContainer>
    ) : Parcelable
}
