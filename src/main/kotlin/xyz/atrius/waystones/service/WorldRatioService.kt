package xyz.atrius.waystones.service

import org.bukkit.Bukkit
import org.bukkit.World
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.data.TeleportType
import xyz.atrius.waystones.internal.KotlinPlugin

@Single
class WorldRatioService(plugin: KotlinPlugin) : JsonFile<Double>("ratios", plugin), Iterable<Pair<String, Double>> {

    operator fun get(world: World): Double =
        data[world.name.lowercase()] ?: 1.0

    fun add(item: String, ratio: Double): Boolean {
        val world = Bukkit.getWorld(item)

        if (world != null) {
            data[world.name.lowercase()] = ratio
            save()
        }

        return world != null
    }

    fun remove(item: String): Boolean {
        val world = Bukkit.getWorld(item)

        if (world != null) {
            data.remove(world.name.lowercase())
            save()
        }

        return world != null
    }

    fun getRatio(type: TeleportType): Double = when (type) {
        is TeleportType.Normal -> 1.0
        is TeleportType.Interdimensional -> get(type.from) / get(type.to)
    }

    override fun iterator(): Iterator<Pair<String, Double>> = Bukkit
        .getWorlds()
        .map { it.name to get(it) }
        .iterator()
}