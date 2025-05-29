package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class BaseDistanceProperty : ConfigProperty<Int>(
    property = "base-distance",
    default = 100,
    parser = IntegerArgumentType.integer(0),
    propertyType = Int::class,
    format = { "$it Block(s)" }
)
