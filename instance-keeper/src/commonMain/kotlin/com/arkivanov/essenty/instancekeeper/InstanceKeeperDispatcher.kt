package com.arkivanov.essenty.instancekeeper

import kotlin.js.JsName

interface InstanceKeeperDispatcher : InstanceKeeper {

    fun destroy()
}

@JsName("instanceKeeperDispatcher")
@Suppress("FunctionName")
fun InstanceKeeperDispatcher(): InstanceKeeperDispatcher = DefaultInstanceKeeperDispatcher()
