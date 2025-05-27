package xyz.atrius.waystones.utility

import org.bukkit.inventory.Recipe
import org.bukkit.scheduler.BukkitScheduler
import xyz.atrius.waystones.data.advancement.Advancement
import xyz.atrius.waystones.data.crafting.CraftingRecipe
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.plugin

fun KotlinPlugin.registerRecipes(vararg recipes: Recipe) = recipes.forEach {
    if (it is CraftingRecipe) {
        it.setup()
    }

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
    if (spigotAdv == null) {
        println(adv.toJson())

        server.unsafe.loadAdvancement(key, adv.toJson())
    }
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