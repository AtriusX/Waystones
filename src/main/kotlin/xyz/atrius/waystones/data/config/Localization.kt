package xyz.atrius.waystones.data.config

import org.bukkit.configuration.file.YamlConfiguration
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.translateColors
import java.io.File
import java.io.InputStreamReader
import java.lang.IllegalArgumentException
import java.text.MessageFormat
import java.util.*
import kotlin.collections.HashMap

class Localization(val plugin: KotlinPlugin, localeTag: String) {
    private var configFile = File(plugin.dataFolder, "locale-$localeTag.yml")
    private val config = YamlConfiguration()
    private val cachedMessageFormat = HashMap<String, MessageFormat>()
    var locale: Locale = Locale.getDefault()

    init {
        reload(localeTag)
    }

    fun localize(key: String, vararg args: Any?): LocalizedString {
        return LocalizedString(key, args)
    }

    fun format(string: String, vararg args: Any?): String {
        return MessageFormat(string, locale).format(args).translateColors()
    }

    fun getTemplate(key: String): MessageFormat {
        val template = config.get(key)
                ?: throw IllegalArgumentException("$key does not exist in localization file!")
        return cachedMessageFormat.getOrPut(key, { MessageFormat(template as String, locale) })
    }

    operator fun get(key: String, vararg args: Any?): LocalizedString {
        return localize(key, *args)
    }

    fun reload(localeTag: String) {
        configFile = File(plugin.dataFolder, "locale-$localeTag.yml")

        if (!configFile.exists()) {
            plugin.saveResource(configFile.name, false)
            config.load(configFile)
        } else {
            val defaultConfigFile = plugin.getResource(configFile.name)
                    ?: plugin.getResource("locale-en.yml")
                    ?: throw RuntimeException("default localization file not in jar, corrupt download?")
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigFile))
            config.load(configFile)
            config.options().copyDefaults(true)
            config.setDefaults(defaultConfig)
            config.save(configFile)
        }

        cachedMessageFormat.clear()
        locale = Locale.forLanguageTag(config.getString("locale"))
    }
}
