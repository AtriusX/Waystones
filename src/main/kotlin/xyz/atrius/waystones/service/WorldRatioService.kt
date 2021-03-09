package xyz.atrius.waystones.service

import org.bukkit.World
import xyz.atrius.waystones.data.JsonFile

object WorldRatioService : JsonFile<Double>("ratios") {

    operator fun get(world: World): Double =
        data.getOrDefault(world.name, 1.0)
}