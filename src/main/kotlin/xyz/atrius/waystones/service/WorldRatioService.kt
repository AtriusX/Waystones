package xyz.atrius.waystones.service

import org.bukkit.World
import xyz.atrius.waystones.data.JsonFile

object WorldRatioService : JsonFile<Double>("ratios") {

    operator fun get(world: World): Double =
        data.getOrDefault(world.environment.name.toLowerCase(), 1.0)

    operator fun get(name: String, asEnvironment: Boolean): Double {
        val key = if (asEnvironment) name else "name:$name"
        return data.getOrDefault(key.toLowerCase(), 1.0)
    }

    fun add(value: String, asEnvironment: Boolean, ratio: Double) {
        val key = if (asEnvironment) value else "name:$value"
        data[key.toLowerCase()] = ratio
        save(true)
    }

    fun remove(value: String, asEnvironment: Boolean) {
        val key = if (asEnvironment) value else "name:$value"
        data.remove(key)
        save(true)
    }
}