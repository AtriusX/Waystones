package xyz.atrius.waystones.utility

import org.bukkit.*
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType.INTEGER
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import xyz.atrius.waystones.plugin

// Just because I'm petty
typealias KotlinPlugin =
        JavaPlugin

private val DEFAULT_LORE = "${ChatColor.DARK_PURPLE}Warpstone: [${ChatColor.MAGIC}UNKNOWN${ChatColor.DARK_PURPLE}]"

fun defaultWarpKey(): ItemStack = ItemStack(Material.COMPASS).update<CompassMeta> {
    this["is_warp_key", INTEGER] = 1
    lore = listOf(DEFAULT_LORE)
    setDisplayName("${ChatColor.GOLD}Warpstone Key")
}

fun PluginManager.registerEvents(vararg listeners: Listener) =
        listeners.forEach { registerEvents(it, plugin) }

fun BukkitScheduler.scheduleRepeatingAutoCancelTask(
    delay: Long, period: Long = 1, task: (Long) -> Unit, finish: Runnable? = null
): Int {
    var timer = delay
    val id = scheduleSyncRepeatingTask(plugin, { task(timer).also { timer-- } }, 0, period)
    scheduleSyncDelayedTask(plugin, {
        if (isQueued(id)) cancelTask(id).also {
            finish?.run()
        }
    }, delay)
    return id
}

// TODO: Consider removing
fun World.forceSpawnParticle(
    particle: Particle, location: Location, count: Int,
    offsetX: Double, offsetY: Double, offsetZ: Double,
    speed: Double, waitTime: Long = 12
) = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
    spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed)
}, if (location.sameDimension(this)) waitTime else 1)