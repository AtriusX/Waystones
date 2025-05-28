package xyz.atrius.waystones.crafting

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.KeyRecipeProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.provider.DefaultKeyProvider
import xyz.atrius.waystones.utility.glue
import xyz.atrius.waystones.utility.hashChar
import xyz.atrius.waystones.utility.sqrt
import xyz.atrius.waystones.utility.toKey

@Single
class CompassRecipe(
    keyRecipe: KeyRecipeProperty,
    plugin: KotlinPlugin,
    defaultKeyProvider: DefaultKeyProvider,
) : CraftingRecipe("is_warp_key".toKey(plugin), defaultKeyProvider.getKey()) {
    private val waystoneKeyRecipe = keyRecipe.value

    override val recipe = run {
        // Pull recipe from config and determine grid size (list can be size 1, 4, or 9)
        val size = sqrt(waystoneKeyRecipe.size)

        waystoneKeyRecipe
            // Map each item to it's hash char
            .map { it.hashChar() }
            // Chunk the list by its square size
            .chunked(size)
            // Glue each list into a string and join each by a newline
            .joinToString("\n", transform = List<Char>::glue)
    }

    override val items = hashMapOf<Char, Material>().apply {
        // For each item in the recipe, map it's material to it's hashcode
        for (item in waystoneKeyRecipe.toHashSet()) {
            if (item.name != "AIR" || item.isEmpty()) {
                this[item.hashChar()] = item
            }
        }
    }
}
