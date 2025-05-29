package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class PowerCostProperty : ConfigProperty<Int>(
    property = "power-cost",
    default = 1,
    parser = IntegerArgumentType.integer(1, 4),
    propertyType = Int::class,
    format = { "$it Points" }
)
