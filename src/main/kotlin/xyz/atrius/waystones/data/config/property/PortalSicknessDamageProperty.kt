package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.DoubleArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class PortalSicknessDamageProperty : ConfigProperty<Double>(
    property = "portal-sickness-damage",
    default = 5.0,
    parser = DoubleArgumentType.doubleArg(0.0),
    propertyType = Double::class,
    format = { "%.1f heart(s)".format(it / 2) }
)
