package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class MaxWarpSizeProperty : ConfigProperty<Int>(
    property = "max-warp-size",
    default = 25,
    parser = IntegerArgumentType.integer(0),
    propertyType = Int::class,
)
