package xyz.atrius.waystones.data.config.property

import xyz.atrius.waystones.command.resolver.LevelArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty

data class Xp(val amount: Int, val asLevels: Boolean) {

    override fun toString(): String = when (asLevels) {
        true -> "$amount Level(s)"
        else -> amount.toString()
    }
}

class MaxXpUsageProperty : ConfigProperty<Xp>(
    property = "max-xp-usage",
    default = Xp(0, false),
    parser = LevelArgumentType,
    propertyType = Xp::class,
    format = { it.toString() },
)