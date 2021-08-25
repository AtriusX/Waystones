package xyz.atrius.waystones.service

import org.bukkit.Bukkit
import org.bukkit.World
import xyz.atrius.waystones.data.JsonFile

object WorldRatioService : JsonFile<Double>("ratios"), Iterable<Map.Entry<String, Double>> {

    operator fun get(world: World): Double =
        data["name${world.name.toLowerCase()}"]
        ?: data[world.environment.name.toLowerCase()]
        ?: 1.0

    fun add(item: String, asEnvironment: Boolean, ratio: Double): Boolean {
        if (!asEnvironment) {
            val world = Bukkit.getWorld(item)
            if (world != null)
                data["name:${world.name.toLowerCase()}"] = ratio
            return world != null
        }
        val env = World.Environment.values().any { it.name == item.toUpperCase() }
        if (env)
            data[item.toLowerCase()] = ratio
        return env
    }

    fun remove(item: String, asEnvironment: Boolean): Boolean {
        if (!asEnvironment) {
            val world = Bukkit.getWorld(item)
            if (world != null)
                data.remove("name:${world.name.toLowerCase()}")
            return world != null
        }
        val env = World.Environment.values().any { it.name == item.toUpperCase() }
        if (env)
            data.remove(item.toLowerCase())
        return env
    }

    fun isEmpty(): Boolean =
        data.isEmpty()

    override fun iterator(): Iterator<Map.Entry<String, Double>> =
        data.iterator()
}