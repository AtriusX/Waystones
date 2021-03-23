package xyz.atrius.waystones.data

import org.bukkit.Location
import xyz.atrius.waystones.localization

sealed class KeyState(val message: String?) {

    object None : KeyState(null)

    object Severed : KeyState(localization["key-severed"])

    object Blocked : KeyState(localization["key-blocked"])

    class Connected(val warp: Location) : KeyState(null)
}
