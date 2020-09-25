package xyz.atrius.waystones.utility

sealed class WarpState {

    object None : WarpState()

    object Unpowered : WarpState()

    object Inhibited : WarpState()

    object Infinite : WarpState()

    object Obstructed : WarpState()

    open class Functional(val range: Int) : WarpState()

    class InterDimension(range: Int) : Functional(range)
}

