package com.arkivanov.essenty.statekeeper

import com.arkivanov.essenty.parcelable.CommonParceler
import com.arkivanov.essenty.parcelable.ParcelReader
import com.arkivanov.essenty.parcelable.ParcelWriter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.parcelable.TypeParceler
import com.arkivanov.essenty.parcelable.readByteArray
import com.arkivanov.essenty.parcelable.writeByteArray
import com.arkivanov.essenty.utils.internal.ExperimentalEssentyApi
import com.arkivanov.essenty.utils.internal.PARCELABLE_DEPRECATED_MESSAGE
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlin.reflect.KClass

/**
 * A key-value storage, typically used to persist data after process death or Android configuration changes.
 */
interface StateKeeper {

    /**
     * Removes and returns a previously saved value for the given [key].
     *
     * @param key a key to look up.
     * @param clazz a [KClass] of the value, used for deserialization.
     * @return the value for the given [key] or `null` if no value is found.
     */
    @Deprecated(
        message = PARCELABLE_DEPRECATED_MESSAGE,
        replaceWith = ReplaceWith("this.consume(key = key, strategy = strategy)"),
    )
    fun <T : Parcelable> consume(key: String, clazz: KClass<out T>): T?

    /**
     * Removes and returns a previously saved value for the given [key].
     *
     * @param key a key to look up.
     * @param strategy a [DeserializationStrategy] for deserializing the value.
     * @return the value for the given [key] or `null` if no value is found.
     */
    @Suppress("DEPRECATION")
    fun <T : Any> consume(key: String, strategy: DeserializationStrategy<T>): T? =
        consume(key = key, clazz = ParcelableData::class)
            ?.bytes
            ?.deserialize(strategy = strategy)

    /**
     * Registers the value [supplier] to be called when it's time to persist the data.
     *
     * @param key a key to be associated with the value.
     * @param supplier a supplier of the value.
     */
    @Deprecated(
        message = PARCELABLE_DEPRECATED_MESSAGE,
        replaceWith = ReplaceWith("this.register(key = key, strategy = strategy, supplier = supplier)"),
    )
    fun <T : Parcelable> register(key: String, supplier: () -> T?)

    /**
     * Registers the value [supplier] to be called when it's time to persist the data.
     *
     * @param key a key to be associated with the value.
     * @param strategy a [SerializationStrategy] for serializing the value.
     * @param supplier a supplier of the value.
     */
    @Suppress("DEPRECATION")
    fun <T : Any> register(key: String, strategy: SerializationStrategy<T>, supplier: () -> T?) {
        register(key = key) {
            supplier()
                ?.serialize(strategy = strategy)
                ?.let(::ParcelableData)
        }
    }

    /**
     * Unregisters a previously registered `supplier` for the given [key].
     */
    fun unregister(key: String)

    /**
     * Checks if a `supplier` is registered for the given [key].
     */
    fun isRegistered(key: String): Boolean

    @TypeParceler<ByteArray, ByteArrayParceler>
    @Parcelize
    private class ParcelableData(val bytes: ByteArray) : Parcelable

    @OptIn(ExperimentalEssentyApi::class)
    private object ByteArrayParceler : CommonParceler<ByteArray> {
        override fun ByteArray.write(writer: ParcelWriter) {
            writer.writeByteArray(this)
        }

        override fun create(reader: ParcelReader): ByteArray =
            reader.readByteArray()
    }
}
