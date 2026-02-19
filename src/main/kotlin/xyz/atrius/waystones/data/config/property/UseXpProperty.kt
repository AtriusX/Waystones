package xyz.atrius.waystones.data.config.property

import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.EnumArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty
import xyz.atrius.waystones.data.config.property.type.Power
import xyz.atrius.waystones.utility.sanitizedStringFormat

@Single
class UseXpProperty : ConfigProperty<Power>(
    property = "use-xp",
    default = Power.NONE,
    parser = EnumArgumentType(Power::class),
    propertyType = Power::class,
    format = { it.sanitizedStringFormat() },
    serialize = { it.name },
)