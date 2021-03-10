package xyz.atrius.waystones.data

import org.bukkit.World
import xyz.atrius.waystones.utility.getRatio

sealed class TeleportType {

    object Normal : TeleportType()

    open fun getRatio(): Double = 1.0

    class Interdimensional(
        private val to: World?,
        private val from: World?
    ) : TeleportType() {

        override fun getRatio(): Double =
            (from?.getRatio() ?: 1.0) / (to?.getRatio() ?: 1.0)
    }
}