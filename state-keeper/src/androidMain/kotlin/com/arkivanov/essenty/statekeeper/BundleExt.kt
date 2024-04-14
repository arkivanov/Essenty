package com.arkivanov.essenty.statekeeper

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

/**
 * Inserts the provided `kotlinx-serialization` [Serializable][kotlinx.serialization.Serializable] value
 * into this [Bundle], replacing any existing value for the given [key].
 * Either [key] or [value] may be `null`.
 */
fun <T : Any> Bundle.putSerializable(key: String?, value: T?, strategy: SerializationStrategy<T>) {
    putParcelable(key, ValueHolder(value = value, bytes = lazy { value?.serialize(strategy) }))
}

/**
 * Returns a `kotlinx-serialization` [Serializable][kotlinx.serialization.Serializable] associated with
 * the given [key], or `null` if no mapping exists for the given [key] or a `null` value is explicitly
 * associated with the [key].
 */
fun <T : Any> Bundle.getSerializable(key: String?, strategy: DeserializationStrategy<T>): T? =
    getParcelableCompat<ValueHolder<T>>(key)?.let { holder ->
        holder.value ?: holder.bytes.value?.deserialize(strategy)
    }

@Suppress("DEPRECATION")
private inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String?): T? =
    classLoader.let { savedClassLoader ->
        try {
            classLoader = T::class.java.classLoader
            getParcelable(key) as T?
        } finally {
            classLoader = savedClassLoader
        }
    }

/**
 * Inserts the provided [SerializableContainer] into this [Bundle],
 * replacing any existing value for the given [key]. Either [key] or [value] may be `null`.
 */
fun Bundle.putSerializableContainer(key: String?, value: SerializableContainer?) {
    putSerializable(key = key, value = value, strategy = SerializableContainer.serializer())
}

/**
 * Returns a [SerializableContainer] associated with the given [key],
 * or `null` if no mapping exists for the given [key] or a `null` value
 * is explicitly associated with the [key].
 */
fun Bundle.getSerializableContainer(key: String?): SerializableContainer? =
    getSerializable(key = key, strategy = SerializableContainer.serializer())

private class ValueHolder<out T : Any>(
    val value: T?,
    val bytes: Lazy<ByteArray?>,
) : Parcelable {
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByteArray(bytes.value)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ValueHolder<Any>> {
        override fun createFromParcel(parcel: Parcel): ValueHolder<Any> =
            ValueHolder(value = null, bytes = lazyOf(parcel.createByteArray()))

        override fun newArray(size: Int): Array<ValueHolder<Any>?> =
            arrayOfNulls(size)
    }
}
