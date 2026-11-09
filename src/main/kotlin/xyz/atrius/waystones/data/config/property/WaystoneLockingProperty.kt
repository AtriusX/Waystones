package xyz.atrius.waystones.data.config.property

import xyz.atrius.waystones.command.resolver.EnumArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty
import xyz.atrius.waystones.data.config.property.type.WaystoneLocking
import xyz.atrius.waystones.utility.sanitizedStringFormat

//@Single(binds = [ConfigProperty::class])
// Not fully support yet, this doesn't load properly
class WaystoneLockingProperty : ConfigProperty<WaystoneLocking>(
    property = "waystone-locking",
    default = WaystoneLocking.NONE,
    parser = EnumArgumentType(WaystoneLocking::class),
    propertyType = WaystoneLocking::class,
    format = { it.sanitizedStringFormat() },
    serialize = { it.name },
)
