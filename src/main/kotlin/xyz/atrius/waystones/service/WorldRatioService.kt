package xyz.atrius.waystones.service

import org.bukkit.World
import xyz.atrius.waystones.data.JsonFile

object WorldRatioService : JsonFile<Double>("ratios"), Iterable<Map.Entry<String, Double>> {

    operator fun get(world: World, asEnvironment: Boolean = false): Double {
        val key = if (asEnvironment) world.environment.name else "name:${world.name}"
        return data.getOrDefault(key.toLowerCase(), 1.0)
    }

    fun add(world: World, asEnvironment: Boolean, ratio: Double) {
        val key = if (asEnvironment) world.environment.name else "name:${world.name}"
        data[key.toLowerCase()] = ratio
        save(true)
    }

    fun remove(world: World, asEnvironment: Boolean) {
        val key = if (asEnvironment) world.environment.name else "name:${world.name}"
        data.remove(key.toLowerCase())
        save(true)
    }

    fun isEmpty(): Boolean =
        data.isEmpty()

    override fun iterator(): Iterator<Map.Entry<String, Double>> =
        data.iterator()
}