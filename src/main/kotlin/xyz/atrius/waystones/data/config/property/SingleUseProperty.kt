package xyz.atrius.waystones.data.config.property

import com.mojang.brigadier.arguments.BoolArgumentType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class SingleUseProperty : ConfigProperty<Boolean>(
    property = "single-use",
    default = false,
    parser = BoolArgumentType.bool(),
    propertyType = Boolean::class,
)