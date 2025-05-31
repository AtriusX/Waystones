package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class MaxBoostProperty : ConfigProperty<Int>(
    property = "max-boost",
    default = 50,
    parser = IntegerArgumentType.integer(0),
    propertyType = Int::class,
    format = { "$it Block(s)" },
)
