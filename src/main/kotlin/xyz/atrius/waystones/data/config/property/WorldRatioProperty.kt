package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.DoubleArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class WorldRatioProperty : ConfigProperty<Double>(
    property = "default-world-ratio",
    default = 1.0,
    parser = DoubleArgumentType.doubleArg(0.0),
    propertyType = Double::class,
    format = { "$it Block(s)" },
)
