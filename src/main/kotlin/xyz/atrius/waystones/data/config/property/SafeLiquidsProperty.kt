package xyz.atrius.waystones.data.config.property

import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.EnumArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty
import xyz.atrius.waystones.data.config.property.type.SafeLiquids
import xyz.atrius.waystones.utility.sanitizedStringFormat

@Single(binds = [ConfigProperty::class])
class SafeLiquidsProperty : ConfigProperty<SafeLiquids>(
    property = "safe-liquids",
    default = SafeLiquids.WATER,
    parser = EnumArgumentType(SafeLiquids::class),
    propertyType = SafeLiquids::class,
    format = { it.sanitizedStringFormat() },
    serialize = { it.name },
)
