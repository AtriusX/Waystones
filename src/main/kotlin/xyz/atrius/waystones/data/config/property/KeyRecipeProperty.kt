package xyz.atrius.waystones.data.config.property

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.EnumArgumentType
import xyz.atrius.waystones.data.config.ListConfigProperty
import java.util.function.IntFunction

class StringList(
    vararg items: Material,
) : MutableList<Material> by mutableListOf(*items) {

    private val values = items.map {
        it.name
            .lowercase()
            .replace("[_ ]".toRegex(), "-")
    }

    override fun toString(): String {
        return "[${values.joinToString(", ")}]"
    }

    @Deprecated("no")
    override fun <T : Any> toArray(generator: IntFunction<Array<T>>): Array<T> {
        return generator.apply(values.size)
    }
}

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
