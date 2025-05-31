package xyz.atrius.waystones.service

import com.google.gson.Gson
import org.bukkit.Bukkit
import org.bukkit.World
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.data.config.property.WorldRatioProperty
import xyz.atrius.waystones.data.json.RatioConfig
import xyz.atrius.waystones.internal.KotlinPlugin
import java.util.UUID

@Single
class WorldRatioService(
    gson: Gson,
    plugin: KotlinPlugin,
    private val defaultRatioProperty: WorldRatioProperty,
) : JsonFile<RatioConfig>("ratios", gson, plugin, RatioConfig::class), Iterable<Map.Entry<World, Double>> {

    override fun load() {
        super.load()

        if (data.defaultWorld != null) {
            return
        }
        // Update the default world to the first found normal world
        data.defaultWorld = Bukkit.getWorlds()
            .firstOrNull { it.environment == World.Environment.NORMAL }
            ?.uid
        // Save the default world if none provided
        save()
    }

    fun get(world: World?): Double {
        if (world == null) {
            return -1.0
        }

        val uuid = world.uid

        if (data.defaultWorld == uuid) {
            return 1.0
        }

        if (data.ratios == null) {
            data.ratios = hashMapOf()
        }

        return data.ratios
            ?.get(uuid)
            ?: defaultRatioProperty.value()
    }

    fun add(world: World?, ratio: Double): Boolean {
        if (world == null || data.defaultWorld == world.uid) {
            return false
        }

        if (data.ratios == null) {
            data.ratios == hashMapOf<UUID, Double>()
        }

        data.ratios?.set(world.uid, ratio)
        save()

        return true
    }

    fun remove(world: World?): Boolean {
        if (world == null) {
            return false
        }

        val removed = data.ratios?.remove(world.uid)

        save()

        return removed != null
    }

    fun default(world: World?): Boolean {
        if (world == null) {
            return false
        }

        val uuid = world.uid

        if (data.ratios == null) {
            data.ratios == hashMapOf<UUID, Double>()
        }

        data.defaultWorld = uuid

        if (uuid in (data.ratios ?: emptyMap())) {
            data.ratios?.remove(uuid)
        }

        save()
        return true
    }

    fun isDefault(world: World): Boolean =
        data.defaultWorld == world.uid

    fun getRatio(from: World, to: World): Double? {
        if (from == to) {
            return 1.0
        }
        val fromRatio = get(from)
        val toRatio = get(to)

        if (fromRatio <= 0.0 || toRatio <= 0.0) {
            return null
        }

        return toRatio / fromRatio
    }

    override fun iterator(): Iterator<Map.Entry<World, Double>> {
        val worlds = Bukkit
            .getWorlds()
            .associateWith { get(it) }

        return worlds
            .iterator()
    }
}
