package xyz.atrius.waystones.data.config.property

import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.LimitedArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty
import java.util.*

@Single
class LocaleProperty : ConfigProperty<Locale>(
    property = "locale",
    default = Locale.ENGLISH,
    parser = LimitedArgumentType<Locale>(Locale.getAvailableLocales()),
    propertyType = Locale::class,
)