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
    status: String = "Unknown",
) : WarpState(status, -2) {

    object None : WarpErrorState("The connection to %s has been severed")

    object Inhibited : WarpErrorState("%s has been suppressed", "Inhibited")

    object Unpowered : WarpErrorState("%s does not currently have power", "Unpowered")

    object Obstructed : WarpErrorState("%s is obstructed and cannot be used", "Obstructed")

    open fun message(name: String): String =
        message.format(name)
}

sealed class WarpActiveState(
    val range: Int
) : WarpState("Active", range) {

    class Active(range: Int) : WarpActiveState(range)

    object Infinite : WarpActiveState(-1)
}
