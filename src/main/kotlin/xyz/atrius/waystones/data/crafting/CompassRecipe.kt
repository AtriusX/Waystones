package xyz.atrius.waystones.data.crafting

import org.bukkit.Material
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.toKey

object CompassRecipe : CraftingRecipe("is_warp_key".toKey(), defaultWarpKey()) {

    override val recipe = """
        | *
        |*x*
        | *
    """.trimMargin()

    override val items = hashMapOf(
        '*' to Material.IRON_INGOT,
        'x' to Material.REDSTONE_BLOCK
    )
}