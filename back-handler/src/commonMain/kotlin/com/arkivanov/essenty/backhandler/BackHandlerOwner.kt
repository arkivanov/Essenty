package com.arkivanov.essenty.backhandler

/**
 * Represents a holder of [BackHandler]. It may be implemented by an arbitrary class, to provide convenient API.
 */
interface BackHandlerOwner {

    val backHandler: BackHandler
}
