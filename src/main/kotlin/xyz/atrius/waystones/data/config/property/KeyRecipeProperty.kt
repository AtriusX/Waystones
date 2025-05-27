package xyz.atrius.waystones.data.config.property

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.EnumArgumentType
import xyz.atrius.waystones.data.config.ListConfigProperty

@Single
class KeyRecipeProperty : ListConfigProperty<Material>(
    property = "key-recipe",
    default = listOf(
        Material.AIR,
        Material.IRON_INGOT,
        Material.AIR,
        Material.IRON_INGOT,
        Material.REDSTONE_BLOCK,
        Material.IRON_INGOT,
        Material.AIR,
        Material.IRON_INGOT,
        Material.AIR,
    ),
    parser = EnumArgumentType(Material::class),
    propertyType = Material::class,
    sizes = setOf(1, 4, 9),
)
