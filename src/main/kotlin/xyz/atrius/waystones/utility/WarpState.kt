package xyz.atrius.waystones.utility

import xyz.atrius.waystones.configuration

sealed class WarpState(
        private val message: String?,
        private val status: String,
        private val range: String = "None"
) {

    object None : WarpState(null, "Unknown")

    object Unpowered : WarpState("%s does not currently have power", "Unpowered")

    object Inhibited : WarpState(null, "Inhibited")

    object Infinite : WarpState(null, "Active", "Infinite")

    object Obstructed : WarpState("%s is obstructed and cannot be used", "Active", "Obstructed")

    class Active(range: Int) : WarpState(null, "Active", range.toString()) {
        override fun message(name: String): String? {
            return if (configuration.limitDistance) super.message(name) else ""
        }
    }

    class InterDimension(range: Int) : WarpState(
            "Cannot locate %s due to dimensional interference", "Active", range.toString()
    ) {
        override fun message(name: String): String? {
            return if (!configuration.jumpWorlds) super.message(name) else ""
        }
    }

    open fun message(name: String): String? =
            message?.format(name)

    override fun toString(): String =
            "Status: $status | Range: $range"
}

