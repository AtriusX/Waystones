package xyz.atrius.waystones.data

import org.bukkit.World

sealed class TeleportType {

    object Normal : TeleportType()

    class Interdimensional(
        val to: World,
        val from: World
    ) : TeleportType()
}
