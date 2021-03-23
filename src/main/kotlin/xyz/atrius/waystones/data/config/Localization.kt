package xyz.atrius.waystones.data.config

import org.bukkit.configuration.file.YamlConfiguration
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.translateColors
import java.io.File
import java.io.InputStreamReader
import java.text.MessageFormat
import java.util.*

class Localization(val plugin: KotlinPlugin, localeTag: String) {
    private var configFile = File(plugin.dataFolder, "locale-$localeTag.yml")
    private val config = YamlConfiguration()
    var locale: Locale = Locale.getDefault()

    init {
        reload(localeTag)
    }

    fun localize(key: String, vararg args: Any?): String {
        val template = config.getString(key)
                ?: throw IllegalArgumentException("$key does not exist in localization file!")
        return format(template, *args)
    }

    fun format(string: String, vararg args: Any?): String {
        return MessageFormat(string, locale).format(args).translateColors()
    }

    operator fun get(key: String, vararg args: Any?): String {
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

        locale = Locale.forLanguageTag(config.getString("locale"))
    }
}
