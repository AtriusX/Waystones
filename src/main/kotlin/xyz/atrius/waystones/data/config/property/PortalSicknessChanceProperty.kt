package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.DoubleArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class PortalSicknessChanceProperty : ConfigProperty<Double>(
    property = "portal-sickness-chance",
    default = 0.05,
    parser = DoubleArgumentType.doubleArg(0.0, 1.0),
    propertyType = Double::class,
)
