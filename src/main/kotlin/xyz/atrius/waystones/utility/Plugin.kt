package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType.INTEGER
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import xyz.atrius.waystones.commands.CommandNamespace
import xyz.atrius.waystones.data.advancement.Advancement
import xyz.atrius.waystones.data.crafting.CraftingRecipe
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.plugin

// Just because I'm petty
typealias KotlinPlugin =
    JavaPlugin

private val DEFAULT_LORE: List<String> = listOf(localization["key-lore"].toString())

fun defaultWarpKey(amount: Int = 1): ItemStack = ItemStack(Material.COMPASS, amount).update<CompassMeta> {
    this["is_warp_key", INTEGER] = 1
    lore = DEFAULT_LORE
    setDisplayName(localization["key-name"].toString())
}

fun KotlinPlugin.registerEvents(vararg listeners: Listener) {
    val events = server.pluginManager
    listeners.forEach { events.registerEvents(it, plugin) }
}

fun KotlinPlugin.registerRecipes(vararg recipes: Recipe) = recipes.forEach {
    if (it is CraftingRecipe)
        it.setup()
    server.addRecipe(it)
}

@Suppress("DEPRECATION")
fun KotlinPlugin.unloadAdvancements(vararg advancements: Advancement) = advancements.forEach {
    server.unsafe.removeAdvancement(it.paired().first)
}

@Suppress("DEPRECATION")
fun KotlinPlugin.loadAdvancements(vararg advancements: Advancement) = advancements.forEach {
    val (key, adv) = it.paired()
    val spigotAdv = server.getAdvancement(key)
    if (spigotAdv == null)
        server.unsafe.loadAdvancement(key, adv.toJson())
}


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

fun KotlinPlugin.registerCommands(vararg commands: Pair<String, CommandExecutor>) = commands.forEach { (command, executor) ->
    getCommand(command)?.setExecutor(executor)
}

fun KotlinPlugin.registerNamespaces(vararg namespaces: CommandNamespace) = namespaces.forEach { e ->
    e.aliases.forEach { getCommand(it)?.setExecutor(e) }
}
