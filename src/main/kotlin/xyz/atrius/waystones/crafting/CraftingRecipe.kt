package xyz.atrius.waystones.crafting

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import xyz.atrius.waystones.utility.splitMultiline

abstract class CraftingRecipe(
    key: NamespacedKey,
    item: ItemStack
) : ShapedRecipe(key, item) {

    abstract fun recipe(): String

    abstract fun items(): HashMap<Char, Material>

    fun setup() {
        // Process the crafting recipe
        shape(*recipe().splitMultiline())
        // Set ingredients for each item
        for ((key, item) in items()) {
            if (item == Material.AIR) {
                continue
            }

            setIngredient(key, item)
        }
    }
}
