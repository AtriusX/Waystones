package xyz.atrius.waystones.data

import xyz.atrius.waystones.configuration

sealed class WarpState(
        private val message: String?,
        private val status: String,
        val range: Int = 0
) {
    object None : WarpState(null, "Unknown")

    object Unpowered : WarpState("%s does not currently have power", "Unpowered")

    object Inhibited : WarpState(null, "Inhibited")

    object Infinite : WarpState(null, "Active", -1)

    object Obstructed : WarpState("%s is obstructed and cannot be used", "Obstructed")

    class Active(range: Int) : WarpState(
            "%s is Active", "Active", range
    ) {
        override fun message(name: String): String? =
                if (configuration.limitDistance()) super.message(name) else ""
    }

    class InterDimension(range: Int) : WarpState(
            "Cannot locate %s due to dimensional interference", "Active", range
    ) {
        override fun message(name: String): String? =
                if (!configuration.jumpWorlds()) super.message(name) else ""
    }

    open fun message(name: String): String? =
            message?.format(name)

    override fun toString(): String {
        val range = when (range) {
            0 -> "None"
            -1 -> "Infinite"
            else -> this.range.toString()
        }
        return "Status: $status | Range: $range"
    }
}

