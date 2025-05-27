package xyz.atrius.waystones.data.crafting

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType.INTEGER
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.property.KeyRecipeProperty
import xyz.atrius.waystones.utility.*

fun defaultWarpKey(
    localization: Localization,
    amount: Int = 1,
): ItemStack = ItemStack(Material.COMPASS, amount).update<CompassMeta> {
    this["is_warp_key", INTEGER] = 1

    val lore = localization["key-lore"]
        .toString()
        .let(Component::text)
    val name = localization["key-name"]
        .toString()
        .let(Component::text)

    lore(listOf(lore))
    displayName(name)
}

@Single
class CompassRecipe(
    keyRecipe: KeyRecipeProperty,
    localization: Localization,
) : CraftingRecipe("is_warp_key".toKey(), defaultWarpKey(localization)) {
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