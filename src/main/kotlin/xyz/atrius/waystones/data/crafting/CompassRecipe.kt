package xyz.atrius.waystones.data.crafting

import org.bukkit.Material
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.utility.*

object CompassRecipe : CraftingRecipe("is_warp_key".toKey(), defaultWarpKey()) {

    private val keyRecipe = configuration.keyRecipe()

    override val recipe = run {
        // Pull recipe from config and determine grid size (list can be size 1, 4, or 9)
        val size = sqrt(keyRecipe.size)
        keyRecipe
            // Map each item to it's hash char
            .map(String::hashChar)
            // Chunk the list by its square size
            .chunked(size)
            // Glue each list into a string and join each by a newline
            .joinToString("\n", transform = List<Char>::glue)
    }

    override val items = hashMapOf<Char, Material>().apply {
        // For each item in the recipe, map it's material to it's hashcode
        for (item in keyRecipe.toHashSet()) this[item.hashChar()] =
            if (item.isEmpty()) Material.AIR else Material.valueOf(item) // Empty items default to air
    }
}