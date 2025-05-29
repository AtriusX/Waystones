package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.DoubleArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class WaitTimeProperty : ConfigProperty<Double>(
    property = "wait-time",
    default = 3.0,
    parser = DoubleArgumentType.doubleArg(0.0),
    propertyType = Double::class,
    format = { "%.1f second(s)".format(it) },
    readProcess = { it * 20.0 },
)
