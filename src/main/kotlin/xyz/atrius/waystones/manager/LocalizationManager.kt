package xyz.atrius.waystones.manager

import arrow.core.mapValuesNotNull
import org.bukkit.configuration.file.YamlConfiguration
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.data.config.property.FallbackLocaleProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import java.io.File
import java.text.MessageFormat
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import org.bukkit.entity.Player

@Single
class LocalizationManager(
    @Provided private val plugin: KotlinPlugin,
    @Named("supportedLocales")
    private val supportedLocales: Set<Locale>,
    @Named("defaultPluginLocale")
    private val defaultPluginLocale: Locale,
    private val fallbackLocale: FallbackLocaleProperty,
) {
    private val configs: Map<Locale, LocaleConfig> = supportedLocales
        .associateWith { LocaleConfig(plugin, it) }

    operator fun get(key: String, vararg args: Any?): LocalizedString {
        val values = configs
            .mapValuesNotNull { (_, v) -> v.getTemplate(key) }

        return LocalizedString(
            key = key,
            args = args,
            values = values,
            fallbackLocale = fallbackLocale.value(),
            defaultLocale = defaultPluginLocale,
        )
    }

    operator fun contains(key: String): Boolean = configs
        .any { (_, v) -> v.getTemplate(key) != null }

    fun translateForPlayer(player: Player, key: String, vararg args: Any?): String {
        return get(key, *args).format(player)
    }

    private data class LocaleConfig(
        @Provided private val plugin: KotlinPlugin,
        private val locale: Locale,
    ) {
        private val file: File = File(
            plugin.dataFolder,
            "locale-${locale.toLanguageTag()}.yml"
        )
        private val config: YamlConfiguration = YamlConfiguration()
        private val cachedFormats: ConcurrentHashMap<String, MessageFormat> = ConcurrentHashMap()

        init {
            plugin.saveResource(file.name, true)

            file
                .runCatching(config::load)
                .onFailure {
                    logger.error("Failed to load configuration ${file.name}! Error: ${it.message}")
                }
        }

        fun getTemplate(key: String): MessageFormat? {
            if (cachedFormats.containsKey(key)) {
                return cachedFormats[key]
            }

            val template = config.getString(key)
                ?: return null

            return cachedFormats.getOrPut(key) {
                MessageFormat(template, locale)
            }
        }
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(LocalizationManager::class.java)
    }
}
