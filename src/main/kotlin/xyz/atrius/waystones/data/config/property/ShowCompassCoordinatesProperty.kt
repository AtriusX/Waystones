package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.BoolArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single(binds = [ConfigProperty::class])
class ShowCompassCoordinatesProperty : ConfigProperty<Boolean>(
    property = "show-compass-coordinates",
    default = true,
    parser = BoolArgumentType.bool(),
    propertyType = Boolean::class,
)
