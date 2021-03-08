package xyz.atrius.waystones.data

import org.bukkit.World
import xyz.atrius.waystones.utility.getRatio

sealed class TeleportType {

    object Normal : TeleportType()

    class Interdimensional(
        private val to: World,
        private val from: World
    ) : TeleportType() {

        fun getRatio(): Double =
            from.getRatio().toDouble() / to.getRatio()
    }
}