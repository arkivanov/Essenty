package com.arkivanov.essenty.statekeeper.base64

internal val dictionary: CharArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()

internal val backDictionary: IntArray = IntArray(0x80) { code ->
    dictionary.indexOf(code.toChar())
}
