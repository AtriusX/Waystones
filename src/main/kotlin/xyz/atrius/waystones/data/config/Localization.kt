package xyz.atrius.waystones.data.config

import org.bukkit.configuration.file.YamlConfiguration
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.LocaleProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import java.io.File
import java.io.InputStreamReader
import java.text.MessageFormat

@Single
class Localization(
    private val plugin: KotlinPlugin,
    private val locale: LocaleProperty,
) {

    private var configFile = File(plugin.dataFolder, "locale-${locale.value.toLanguageTag()}.yml")
    private val config = YamlConfiguration()
    private val cachedMessageFormat = HashMap<String, MessageFormat>()

    init {
        reload()
    }

    fun localize(key: String, vararg args: Any?): LocalizedString {
        return LocalizedString(key, this, *args)
    }

//    fun format(string: String, vararg args: Any?): String {
//        return MessageFormat(string, locale.value).format(args).translateColors()
//    }

    fun getTemplate(key: String): MessageFormat {
        val template = config.get(key)
            ?: throw IllegalArgumentException("$key does not exist in localization file!")
        return cachedMessageFormat.getOrPut(key, { MessageFormat(template as String, locale.value) })
    }

    operator fun get(key: String, vararg args: Any?): LocalizedString {
        return localize(key, *args)
    }

    fun reload() {
        configFile = File(plugin.dataFolder, "locale-${locale.value.toLanguageTag()}.yml")

        if (!configFile.exists()) {
            plugin.saveResource(configFile.name, false)
            config.load(configFile)
        } else {
            val defaultConfigFile = plugin.getResource(configFile.name)
                ?: plugin.getResource("locale-en.yml")
                ?: error("default localization file not in jar, corrupt download?")
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigFile))
            config.load(configFile)
            config.options().copyDefaults(true)
            config.setDefaults(defaultConfig)
            config.save(configFile)
        }

        cachedMessageFormat.clear()
    }
}
