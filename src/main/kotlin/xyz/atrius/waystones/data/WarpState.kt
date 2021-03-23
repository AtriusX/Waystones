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
    private val message: LocalizedString,
    status: LocalizedString = localization["waystone-status-unknown"],
) : WarpState(status, -2) {

    object None : WarpErrorState(localization["warp-error"])

    object Inhibited : WarpErrorState(localization["warp-error-inhibited"],
            localization["waystone-status-inhibited"])

    object Unpowered : WarpErrorState(localization["warp-error-unpowered"],
            localization["waystone-status-unpowered"])

    object Obstructed : WarpErrorState(localization["warp-error-obstructed"],
            localization["waystone-status-obstructed"])

    open fun message(name: String): String =
            message.format(name)
}

sealed class WarpActiveState(
    val range: Int
) : WarpState(localization["waystone-status-active"], range) {

    class Active(range: Int) : WarpActiveState(range)

    object Infinite : WarpActiveState(-1)
}
