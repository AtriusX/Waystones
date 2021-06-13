package xyz.atrius.waystones.data.crafting

import org.bukkit.Material
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.toKey
import kotlin.math.sqrt

object CompassRecipe : CraftingRecipe("is_warp_key".toKey(), defaultWarpKey()) {

    override val recipe = run {
        // Pull recipe from config and determine grid size (list can be size 1, 4, or 9)
        val recipe = configuration.keyRecipe()
        val size = sqrt(recipe.size.toFloat()).toInt()
        // Map each item to a hashcode and join them into a multiline string
        recipe.map { it.hashCode() }
            .chunked(size).joinToString("\n") { it.joinToString("") }
    }

    override val items = hashMapOf<Char, Material>().apply {
        // For each item in the recipe, map it's material to it's hashcode
        for (item in configuration.keyRecipe().toHashSet())
            this[item.hashCode().toChar()] =
                if (item.isEmpty()) Material.AIR else Material.valueOf(item)
    }
}