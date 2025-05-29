package xyz.atrius.waystones.data.config.property

import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.LimitedArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty
import java.util.Locale

@Single
class LocaleProperty(
    @Named("supportedLocales")
    private val supportedLocales: Set<Locale>,
) : ConfigProperty<Locale>(
    property = "locale",
    default = Locale.ENGLISH,
    parser = LimitedArgumentType<Locale>(supportedLocales.toTypedArray()),
    propertyType = Locale::class,
)
