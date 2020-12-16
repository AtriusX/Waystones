package xyz.atrius.waystones.data.crafting

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

abstract class CraftingRecipe(
    key: NamespacedKey,
    item: ItemStack
) : ShapedRecipe(key, item) {

    abstract val recipe: String

    abstract val items: HashMap<Char, Material>
}