package xyz.atrius.waystones.utility

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Location
import org.bukkit.Material.*
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.data.FloodFill
import kotlin.math.floor

// Just because I'm petty
typealias KotlinPlugin =
        JavaPlugin

val Location.UP: Location
    get() = this.clone().add(0.0, 1.0, 0.0)

val Location.DOWN: Location
    get() = this.clone().add(0.0, -1.0, 0.0)

val Location.NORTH: Location
    get() = this.clone().add(0.0, 0.0, -1.0)

val Location.SOUTH: Location
    get() = this.clone().add(0.0, 0.0, 1.0)

val Location.WEST: Location
    get() = this.clone().add(-1.0, 0.0, 0.0)

val Location.EAST: Location
    get() = this.clone().add(1.0, 0.0, 0.0)

val Location.center: Location
    get() = apply {
        x = floor(x) + 0.5
        z = floor(z) + 0.5
    }

val Location.neighbors: List<Location>
    get() = listOf(UP, DOWN, NORTH, SOUTH, WEST, EAST)

// Returns the code of this location
val Location.locationCode
    get() = "${world?.name}@$blockX:$blockY:$blockZ"

// Determines if the selected block is safe to spawn on
val Location.isSafe: Boolean
    get() = !listOf(UP, UP.UP).map { world?.getBlockAt(it)?.type?.isSolid ?: true }.any { it }

val Location.powerBlock: Block?
    get() = world?.getBlockAt(DOWN)?.takeIf { it.type == RESPAWN_ANCHOR }

val Location.isPowered: Boolean
    get() {
        val block = powerBlock ?: return false
        val meta = block.blockData as RespawnAnchor
        return meta.charges > 0
    }

fun Player.sendActionMessage(message: String) =
    this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))

inline fun <reified T : Enum<T>> valueOfOrDefault(value: String?, default: T) = try {
    if (value != null) enumValueOf(value) else default
} catch (ignored: Exception) {
    ignored.printStackTrace()
    default
}

fun PluginManager.registerEvents(plugin: JavaPlugin, vararg listeners: Listener) =
    listeners.forEach { registerEvents(it, plugin) }

fun BukkitScheduler.scheduleRepeatingAutoCancelTask(
        plugin: KotlinPlugin,
        period: Long,
        delay : Long,
        task  : Runnable,
        finish: Runnable
): Int {
    val id = scheduleSyncRepeatingTask(plugin, task, 0, period)
    scheduleSyncDelayedTask(plugin, {
        if (isQueued(id)) {
            finish.run()
            cancelTask(id)
        }
    }, delay)
    return id
}

fun Location.range(
    config  : Config
) = config.baseDistance + FloodFill(
    this, config.maxWarpSize, NETHERITE_BLOCK, EMERALD_BLOCK, DIAMOND_BLOCK, GOLD_BLOCK, IRON_BLOCK
).breakdown.entries.sumBy {
    it.value * when(it.key) {
        NETHERITE_BLOCK -> config.netheriteBoost
        EMERALD_BLOCK   -> config.emeraldBoost
        DIAMOND_BLOCK   -> config.diamondBoost
        GOLD_BLOCK      -> config.goldBoost
        IRON_BLOCK      -> config.ironBoost
        else            -> 1
    }
}