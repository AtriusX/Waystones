package xyz.atrius.waystones.data.crafting

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.KeyRecipeProperty
import xyz.atrius.waystones.utility.*

@Single
class CompassRecipe(
    keyRecipe: KeyRecipeProperty,
) : CraftingRecipe("is_warp_key".toKey(), defaultWarpKey()) {

    private val compassRecipe = keyRecipe.value

    override val recipe = run {
        // Pull recipe from config and determine grid size (list can be size 1, 4, or 9)
        val size = sqrt(compassRecipe.size)

        compassRecipe
            // Map each item to it's hash char
            .map { it.hashChar() }
            // Chunk the list by its square size
            .chunked(size)
            // Glue each list into a string and join each by a newline
            .joinToString("\n", transform = List<Char>::glue)
    }

    override val items = hashMapOf<Char, Material>().apply {
        // For each item in the recipe, map it's material to it's hashcode
        for (item in compassRecipe.toHashSet()) {
            if (item.name != "AIR" || item.isEmpty()) {
                this[item.hashChar()] = item
            }
        }
    }
}