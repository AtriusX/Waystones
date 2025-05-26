package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class WorldRatioProperty : ConfigProperty<Int>(
    property = "world-ratio",
    default = 60,
    parser = IntegerArgumentType.integer(1),
    propertyType = Int::class,
)