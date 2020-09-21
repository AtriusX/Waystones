package xyz.atrius.waystones.utility

import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

// Just because I'm petty
typealias KotlinPlugin =
        JavaPlugin

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
        if (isQueued(id)) cancelTask(id).also {
            finish.run()
        }
    }, delay)
    return id
}