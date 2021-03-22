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
        return MessageFormat(config.getString(key, "")!!, locale)
                .format(*args.map { it ?: "null" }.toTypedArray()).translateColors()
    }

    private fun saveDefaults() {
        if (!configFile.exists()) {
            plugin.saveResource(configFile.name, false)
        } else {
            val defaultConfigFile = plugin.getResource(configFile.name)
            if (defaultConfigFile != null) {
                val existingConfig = YamlConfiguration.loadConfiguration(configFile)
                val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigFile))
                existingConfig.setDefaults(defaultConfig)
                existingConfig.save(configFile)
            }
        }
    }

    fun reload(localeTag: String) {
        configFile = File(plugin.dataFolder, "locale-$localeTag.yml")

        saveDefaults()
        config.load(configFile)
        locale = Locale.forLanguageTag(config.getString("locale"))

        val defaultConfigFile = plugin.getResource(configFile.name) ?: plugin.getResource("locale-en.yml")!!
        val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigFile))
        config.setDefaults(defaultConfig)
    }
}
