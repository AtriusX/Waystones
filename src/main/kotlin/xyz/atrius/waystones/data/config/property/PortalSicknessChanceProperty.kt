package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.DoubleArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class PortalSicknessChanceProperty : ConfigProperty<Double>(
    property = "portal-sickness-chance",
    default = 5.0,
    parser = DoubleArgumentType.doubleArg(0.0, 100.0),
    propertyType = Double::class,
    format = { "$it%" },
    readProcess = { it / 100 }
)