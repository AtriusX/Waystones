package xyz.atrius.waystones.service

import org.bukkit.World
import xyz.atrius.waystones.data.JsonFile

object WorldRatioService : JsonFile<Int>("ratios") {

    operator fun get(world: World): Int =
        data[world.name] ?: 1
}