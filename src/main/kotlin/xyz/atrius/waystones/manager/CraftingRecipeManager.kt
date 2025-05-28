package xyz.atrius.waystones.manager

import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.crafting.CraftingRecipe
import xyz.atrius.waystones.internal.KotlinPlugin

@Single
class CraftingRecipeManager(
    private val plugin: KotlinPlugin,
    private val recipes: List<CraftingRecipe>,
) {

    fun load() {
        logger.info("Loading recipes!")

        for (recipe in recipes) {
            recipe.setup()
            plugin.server.addRecipe(recipe)
        }

        logger.info("Recipes loaded!")
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(CraftingRecipeManager::class.java)
    }
}