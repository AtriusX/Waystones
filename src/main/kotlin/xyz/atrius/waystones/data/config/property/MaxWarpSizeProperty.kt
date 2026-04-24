package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single(binds = [ConfigProperty::class])
class MaxWarpSizeProperty : ConfigProperty<Int>(
    property = "max-warp-size",
    default = 25,
    parser = IntegerArgumentType.integer(0),
    propertyType = Int::class,
    format = { "$it Block(s) Max" }
)
