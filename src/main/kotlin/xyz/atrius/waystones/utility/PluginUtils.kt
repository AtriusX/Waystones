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
import xyz.atrius.waystones.data.Config

// Just because I'm petty
typealias KotlinPlugin =
        JavaPlugin

fun keyValue(plugin: KotlinPlugin) =
    NamespacedKey(plugin, "is_warp_key")

fun defaultWarpKey(plugin: KotlinPlugin): ItemStack = ItemStack(Material.COMPASS).update<CompassMeta> {
    persistentDataContainer[keyValue(plugin), PersistentDataType.INTEGER] = 1
    lore = listOf(
        "${ChatColor.DARK_PURPLE}Warpstone: [${ChatColor.MAGIC}UNKNOWN${ChatColor.DARK_PURPLE}]"
    )
    setDisplayName("${ChatColor.GOLD}Warpstone Key")
}

fun ItemStack.isWarpKey(plugin: KotlinPlugin, config: Config) = if (config.keyItems)
    itemMeta?.persistentDataContainer?.get(keyValue(plugin), PersistentDataType.INTEGER) == 1
else type == Material.COMPASS

fun PluginManager.registerEvents(plugin: JavaPlugin, vararg listeners: Listener) =
    listeners.forEach { registerEvents(it, plugin) }

fun BukkitScheduler.scheduleRepeatingAutoCancelTask(
        plugin: KotlinPlugin,
        period: Long,
        delay : Long,
        task  : (Long) -> Unit,
        finish: Runnable
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