package xyz.atrius.waystones.utility

import org.bukkit.Location

sealed class KeyState {

    object None : KeyState()

    object Severed : KeyState()

    object Blocked : KeyState()

    class Connected(val warp: Location?) : KeyState()
}
