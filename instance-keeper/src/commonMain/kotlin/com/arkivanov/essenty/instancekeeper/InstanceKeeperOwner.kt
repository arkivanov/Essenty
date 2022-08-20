package com.arkivanov.essenty.instancekeeper

/**
 * Represents a holder of [InstanceKeeper].
 */
interface InstanceKeeperOwner {

    val instanceKeeper: InstanceKeeper
}
