package xyz.atrius.waystones.data

sealed class WarpState(
    private val status: String
) {
    override fun toString(): String {
        return "Status: $status"
    }
}

sealed class WarpErrorState(
    private val message: String,
    status: String = "Unknown",
) : WarpState(status) {

    object None : WarpErrorState("The connection to %s has been severed")

    object Inhibited : WarpErrorState("%s has been suppressed", "Inhibited")

    object Unpowered : WarpErrorState("%s does not currently have power", "Unpowered")

    object Obstructed : WarpErrorState("%s is obstructed and cannot be used", "Obstructed")

    open fun message(name: String): String =
        message.format(name)
}

sealed class WarpActiveState(
    val range: Int
) : WarpState("Active") {

    class Active(range: Int) : WarpActiveState(range)

    object Infinite : WarpActiveState(-1)

    override fun toString(): String {
        val range = when (range) {
            -1 -> "Infinite"
            else -> this.range.toString()
        }
        return "${super.toString()} | Range: $range"
    }
}