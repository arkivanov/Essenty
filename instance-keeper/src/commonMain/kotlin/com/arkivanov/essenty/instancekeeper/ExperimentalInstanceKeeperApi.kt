package com.arkivanov.essenty.instancekeeper

/**
 * Marks experimental API in Essenty. An experimental API can be changed or removed at any time.
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class ExperimentalInstanceKeeperApi