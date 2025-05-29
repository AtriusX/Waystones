package xyz.atrius.waystones.manager

import arrow.core.mapValuesNotNull
import org.bukkit.configuration.file.YamlConfiguration
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.data.config.property.LocaleProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import java.io.File
import java.text.MessageFormat
import java.util.Locale

@Single
class LocalizationManager(
    private val plugin: KotlinPlugin,
    @Named("supportedLocales")
    private val supportedLocales: Set<Locale>,
    @Named("defaultPluginLocale")
    private val defaultPluginLocale: Locale,
    private val preferredPluginLocale: LocaleProperty,
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
            preferredPluginLocale = preferredPluginLocale.value,
            defaultLocale = defaultPluginLocale,
        )
    }

    private data class LocaleConfig(
        private val plugin: KotlinPlugin,
        private val locale: Locale,
    ) {
        private val file: File = File(
            plugin.dataFolder,
            "locale-${locale.toLanguageTag()}.yml"
        )
        private val config: YamlConfiguration = YamlConfiguration()
        private val cachedFormats: HashMap<String, MessageFormat> = hashMapOf()

        init {
            plugin.saveResource(file.name, true)

            file
                .runCatching(config::load)
                .onFailure {
                    logger.error("Failed to load configuration ${file.name}! Error: ${it.message}")
                }
        }

        fun getTemplate(key: String): MessageFormat? {
            if (key in cachedFormats) {
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