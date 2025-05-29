package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class WaitTimeProperty : ConfigProperty<Int>(
    property = "wait-time",
    default = 60,
    parser = IntegerArgumentType.integer(0),
    propertyType = Int::class,
    format = { "${it / 20.0} second(s)" }
)
