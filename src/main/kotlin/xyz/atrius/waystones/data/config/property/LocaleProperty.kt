package xyz.atrius.waystones.data.config.property

import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.LimitedArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty
import xyz.atrius.waystones.localization
import java.util.*

@Single
class LocaleProperty : ConfigProperty<Locale>(
    property = "locale",
    default = Locale.ENGLISH,
    parser = LimitedArgumentType<Locale>(Locale.getAvailableLocales()),
    onUpdate = localization::reload,
    propertyType = Locale::class,
)