package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

/**
 * Inserts the provided [SerializableContainer] into this [Bundle],
 * replacing any existing value for the given [key]. Either [key] or [value] may be `null`.
 */
fun Bundle.putSerializableContainer(key: String?, value: SerializableContainer?) {
    putParcelable(key, value?.let(::SerializableContainerWrapper))
}

/**
 * Returns a [SerializableContainer] associated with the given [key],
 * or `null` if no mapping exists for the given [key] or a `null` value
 * is explicitly associated with the [key].
 */
@Suppress("DEPRECATION")
fun Bundle.getSerializableContainer(key: String?): SerializableContainer? =
    classLoader.let { savedClassLoader ->
        try {
            classLoader = SerializableContainerWrapper::class.java.classLoader
            (getParcelable(key) as SerializableContainerWrapper?)?.container
        } finally {
            classLoader = savedClassLoader
        }
    }

private class SerializableContainerWrapper(
    val container: SerializableContainer,
) : Parcelable {
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByteArray(container.serialize(strategy = SerializableContainer.serializer()))
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SerializableContainerWrapper> {
        override fun createFromParcel(parcel: Parcel): SerializableContainerWrapper =
            SerializableContainerWrapper(requireNotNull(parcel.createByteArray()).deserialize(SerializableContainer.serializer()))

        override fun newArray(size: Int): Array<SerializableContainerWrapper?> =
            arrayOfNulls(size)
    }
}
