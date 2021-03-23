package xyz.atrius.waystones.data

import xyz.atrius.waystones.localization

sealed class WarpState(
    private val status: String,
    private val range: Int
) {
    override fun toString(): String {
        return localization["waystone-status", status, range]
    }
}

sealed class WarpErrorState(
    private val message: String,
    status: String = localization["waystone-status-unknown"],
) : WarpState(status, -2) {

    object None : WarpErrorState(localization["warp-error"])

    object Inhibited : WarpErrorState(localization["warp-error-inhibited"],
            localization["waystone-status-inhibited"])

    object Unpowered : WarpErrorState(localization["warp-error-unpowered"],
            localization["waystone-status-unpowered"])

    object Obstructed : WarpErrorState(localization["warp-error-obstructed"],
            localization["waystone-status-obstructed"])

    open fun message(name: String): String =
        localization.format(message, name)
}

sealed class WarpActiveState(
    val range: Int
) : WarpState(localization["waystone-status-active"], range) {

    class Active(range: Int) : WarpActiveState(range)

    object Infinite : WarpActiveState(-1)
}
