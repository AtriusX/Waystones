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
    plugin: KotlinPlugin,
    defaultKeyProvider: DefaultKeyProvider,
    private val keyRecipe: KeyRecipeProperty,
) : CraftingRecipe("is_warp_key".toKey(plugin), defaultKeyProvider.getKey()) {

    override fun recipe(): String {
        val waystoneKeyRecipe = keyRecipe.value()
        // Pull recipe from config and determine grid size (list can be size 1, 4, or 9)
        val size = sqrt(waystoneKeyRecipe.size)

        return waystoneKeyRecipe
            // Map each item to it's hash char
            .map { it.hashChar() }
            // Chunk the list by its square size
            .chunked(size)
            // Glue each list into a string and join each by a newline
            .joinToString("\n", transform = List<Char>::glue)
    }

    override fun items(): HashMap<Char, Material> = hashMapOf<Char, Material>().apply {
        // For each item in the recipe, map it's material to it's hashcode
        for (item in keyRecipe.value().toHashSet()) {
            if (!item.isAir) {
                this[item.hashChar()] = item
            }
        }
    }
}
