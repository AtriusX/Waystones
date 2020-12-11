package xyz.atrius.waystones.utility

import org.bukkit.Location

sealed class KeyState(val message: String?) {

    object None : KeyState(null)

    object Severed : KeyState("The link to this warpstone has been severed")

    object Blocked : KeyState("You are too sick to warp")

    class Connected(val warp: Location) : KeyState(null)
}
