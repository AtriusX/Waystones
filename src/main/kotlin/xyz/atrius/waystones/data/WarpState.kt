package xyz.atrius.waystones.data

import xyz.atrius.waystones.data.config.LocalizedString
import xyz.atrius.waystones.localization

sealed class WarpState(
    private val status: LocalizedString,
    private val range: Int
) {
    override fun toString(): String {
        return toLocalizedString().toString()
    }

    fun toLocalizedString(): LocalizedString {
        return localization["waystone-status", status, range]
    }
}

sealed class WarpErrorState(
    val message_key: String,
    status: LocalizedString = localization["waystone-status-unknown"],
) : WarpState(status, -2) {

    object None : WarpErrorState("warp-error")

    object Inhibited : WarpErrorState("warp-error-inhibited",
            localization["waystone-status-inhibited"])

    object Unpowered : WarpErrorState("warp-error-unpowered",
            localization["waystone-status-unpowered"])

    object Obstructed : WarpErrorState("warp-error-obstructed",
            localization["waystone-status-obstructed"])
}

sealed class WarpActiveState(
    val range: Int
) : WarpState(localization["waystone-status-active"], range) {

    class Active(range: Int) : WarpActiveState(range)

    object Infinite : WarpActiveState(-1)
}
