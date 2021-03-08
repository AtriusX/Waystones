package xyz.atrius.waystones.handler

sealed class HandleState {

    object Success : HandleState()

    object Ignore : HandleState()

    class Fail(val error: String) : HandleState()
}