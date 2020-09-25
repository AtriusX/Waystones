package xyz.atrius.waystones.utility

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import xyz.atrius.waystones.plugin

// Just because I'm petty
typealias KotlinPlugin =
    JavaPlugin

fun keyValue() =
    NamespacedKey(plugin, "is_warp_key")

fun defaultWarpKey(): ItemStack = ItemStack(Material.COMPASS).update<CompassMeta> {
    persistentDataContainer[keyValue(), PersistentDataType.INTEGER] = 1
    lore = listOf(
        "${ChatColor.DARK_PURPLE}Warpstone: [${ChatColor.MAGIC}UNKNOWN${ChatColor.DARK_PURPLE}]"
    )
    setDisplayName("${ChatColor.GOLD}Warpstone Key")
}

fun PluginManager.registerEvents(vararg listeners: Listener) =
    listeners.forEach { registerEvents(it, plugin) }

fun BukkitScheduler.scheduleRepeatingAutoCancelTask(
    delay : Long,
    task  : (Long) -> Unit,
    finish: Runnable,
    period: Long = 1
): Int {
    var timer = delay
    val id = scheduleSyncRepeatingTask(plugin, { task(timer).also { timer-- } }, 0, period)
    scheduleSyncDelayedTask(plugin, {
        if (isQueued(id)) cancelTask(id).also {
            finish.run()
        }
    }, delay)
    return id
}