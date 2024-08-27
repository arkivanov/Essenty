package com.arkivanov.essenty.statekeeper

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import com.arkivanov.essenty.statekeeper.base64.base64ToByteArray
import com.arkivanov.essenty.statekeeper.base64.toBase64
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

/**
 * Inserts the provided `kotlinx-serialization` [Serializable][kotlinx.serialization.Serializable] value
 * into this [PersistableBundle], replacing any existing value for the given [key].
 * Either [key] or [value] may be `null`.
 *
 * **Note:** unlike [Bundle.putSerializable], due to the specifics of [PersistableBundle] this function
 * serializes the [value] eagerly, which may degrade the performance for large payloads.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun <T : Any> PersistableBundle.putSerializable(key: String?, value: T?, strategy: SerializationStrategy<T>) {
    putString(key, value?.serialize(strategy)?.toBase64())
}

/**
 * Returns a `kotlinx-serialization` [Serializable][kotlinx.serialization.Serializable] associated with
 * the given [key], or `null` if no mapping exists for the given [key] or a `null` value is explicitly
 * associated with the [key].
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun <T : Any> PersistableBundle.getSerializable(key: String?, strategy: DeserializationStrategy<T>): T? =
    getString(key)?.base64ToByteArray()?.deserialize(strategy)

/**
 * Inserts the provided [SerializableContainer] into this [Bundle],
 * replacing any existing value for the given [key]. Either [key] or [value] may be `null`.
 *
 * **Note:** unlike [Bundle.putSerializableContainer], due to the specifics of [PersistableBundle]
 * this function serializes the [value] eagerly, which may degrade the performance for large payloads.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun PersistableBundle.putSerializableContainer(key: String?, value: SerializableContainer?) {
    putSerializable(key = key, value = value, strategy = SerializableContainer.serializer())
}

/**
 * Returns a [SerializableContainer] associated with the given [key],
 * or `null` if no mapping exists for the given [key] or a `null` value
 * is explicitly associated with the [key].
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun PersistableBundle.getSerializableContainer(key: String?): SerializableContainer? =
    getSerializable(key = key, strategy = SerializableContainer.serializer())
