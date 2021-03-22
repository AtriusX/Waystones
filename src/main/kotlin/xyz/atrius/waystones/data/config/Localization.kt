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
    private var locale: Locale = Locale.getDefault()

    init {
        reload(localeTag)
    }

    fun localize(key: String, vararg args: Any?): String {
        return MessageFormat(config.getString(key)!!, locale)
                .format(*args.map { it ?: "null" }.toTypedArray()).translateColors()
    }

    fun reload(localeTag: String) {
        configFile = File(plugin.dataFolder, "locale-$localeTag.yml")

        if (!configFile.exists()) {
            plugin.saveResource(configFile.name, false)
            config.load(configFile)
        } else {
            val defaultConfigFile = plugin.getResource(configFile.name) ?: plugin.getResource("locale-en.yml")!!
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigFile))
            config.load(configFile)
            config.options().copyDefaults(true)
            config.setDefaults(defaultConfig)
            config.save(configFile)
        }

        locale = Locale.forLanguageTag(config.getString("locale"))
    }
}
