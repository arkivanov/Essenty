package com.arkivanov.essenty.statekeeper

/**
 * Represents a holder of [StateKeeper].
 */
interface StateKeeperOwner {

    val stateKeeper: StateKeeper
}
