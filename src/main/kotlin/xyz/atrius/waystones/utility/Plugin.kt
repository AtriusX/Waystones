package xyz.atrius.waystones.utility

import org.bukkit.inventory.Recipe
import xyz.atrius.waystones.crafting.CraftingRecipe
import xyz.atrius.waystones.internal.KotlinPlugin

fun KotlinPlugin.registerRecipes(vararg recipes: Recipe) = recipes.forEach {
    if (it is CraftingRecipe) {
        it.setup()
    }

    server.addRecipe(it)
}

fun KotlinPlugin.scheduleRepeatingAutoCancelTask(
    delay: Long,
    period: Long = 1,
    task: (Long) -> Unit,
    finish: Runnable? = null
): Int {
    var timer = delay
    val scheduler = server.scheduler
    val id = scheduler.scheduleSyncRepeatingTask(
        this,
        { task(timer).also { timer-- } },
        0,
        period
    )

    scheduler.scheduleSyncDelayedTask(this, {
        if (scheduler.isQueued(id)) {
            scheduler
                .cancelTask(id)
                .also { finish?.run() }
        }
    }, delay)

    return id
}
