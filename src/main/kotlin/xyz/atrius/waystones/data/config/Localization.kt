package xyz.atrius.waystones.data.config

import org.bukkit.configuration.file.YamlConfiguration
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.translateColors
import java.io.File
import java.io.InputStreamReader
import java.text.MessageFormat
import java.util.*

class Localization(val plugin: KotlinPlugin, locale: String) {
    private var configFile = File(plugin.dataFolder, "locale-$locale.yml")
    val config = YamlConfiguration()

    init {
        reload(locale)
    }

    fun localize(key: String, vararg args: Any?): String {
        return MessageFormat(config.getString(key, "")!!, Locale.forLanguageTag(config.getString("locale")))
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

    fun reload(locale: String) {
        configFile = File(plugin.dataFolder, "locale-$locale.yml")

        saveDefaults()
        config.load(configFile)

        val defaultConfigFile = plugin.getResource(configFile.name) ?: plugin.getResource("locale-en.yml")!!
        val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(defaultConfigFile))
        config.setDefaults(defaultConfig)
    }
}
