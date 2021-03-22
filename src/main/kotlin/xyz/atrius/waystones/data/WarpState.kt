package xyz.atrius.waystones.data

import xyz.atrius.waystones.localization

sealed class WarpState(
    private val status: String,
    private val range: Int
) {
    override fun toString(): String {
        return localization.localize("waystone-status", status, range)
    }
}

sealed class WarpErrorState(
    private val message: String,
    status: String = localization.localize("waystone-status-unknown"),
) : WarpState(status, -2) {

    object None : WarpErrorState(localization.localize("warp-error"))

    object Inhibited : WarpErrorState(localization.localize("warp-error-inhibited"),
            localization.localize("waystone-status-inhibited"))

    object Unpowered : WarpErrorState(localization.localize("warp-error-unpowered"),
            localization.localize("waystone-status-unpowered"))

    object Obstructed : WarpErrorState(localization.localize("warp-error-obstructed"),
            localization.localize("waystone-status-obstructed"))

    open fun message(name: String): String =
        message.format(name)
}

sealed class WarpActiveState(
    val range: Int
) : WarpState(localization.localize("waystone-status-active"), range) {

    class Active(range: Int) : WarpActiveState(range)

    object Infinite : WarpActiveState(-1)
}
