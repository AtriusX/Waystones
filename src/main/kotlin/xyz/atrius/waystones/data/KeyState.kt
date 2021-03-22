package xyz.atrius.waystones.data

import org.bukkit.Location
import xyz.atrius.waystones.localization

sealed class KeyState(val message: String?) {

    object None : KeyState(null)

    object Severed : KeyState(localization.localize("key-severed"))

    object Blocked : KeyState(localization.localize("key-blocked"))

    class Connected(val warp: Location) : KeyState(null)
}
