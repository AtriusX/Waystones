package xyz.atrius.waystones.handler

import xyz.atrius.waystones.data.config.LocalizedString

sealed class HandleState {

    object Success : HandleState()

    object Ignore : HandleState()

    class Fail(val error: LocalizedString) : HandleState()
}
