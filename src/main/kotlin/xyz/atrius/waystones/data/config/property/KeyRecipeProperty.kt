package xyz.atrius.waystones.data.config.property

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.EnumArgumentType
import xyz.atrius.waystones.data.config.ListConfigProperty
import xyz.atrius.waystones.utility.sanitizedStringFormat

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
    format = {
        val size = when (it.size) {
            1 -> 1
            4 -> 2
            else -> 3
        }
        val rows = it
            .map { m -> "[ ${m.name.sanitizedStringFormat()} ]" }
            .chunked(size)
            .mapIndexed { i, row ->
                val row = row.joinToString(" ")
                "| - Row %d: %s".format(i + 1, row)
            }

        "\n%s".format(rows.joinToString("\n"))
    },
    serialize = { it.map { m -> m.name } }
)
