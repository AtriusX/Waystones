package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.BoolArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class DamageStopsWarpingProperty : ConfigProperty<Boolean>(
    property = "damage-stops-warping",
    default = true,
    parser = BoolArgumentType.bool(),
    propertyType = Boolean::class
)
