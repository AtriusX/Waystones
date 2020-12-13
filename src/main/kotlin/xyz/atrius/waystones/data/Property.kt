package xyz.atrius.waystones.data

import org.bukkit.configuration.file.FileConfiguration
import xyz.atrius.waystones.data.config.ArgumentParser
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.plugin
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class Property<T>(
    property: String,
    default: T,
    private val parser: ArgumentParser<T>
) {
    private val config: FileConfiguration
        get() = plugin.config

    init {
        ConfigManager.register(property, this)
    }

    private var value: T by observable(
        parser.parse(config.getString(property)) ?: default, observe(property)
    )

    operator fun invoke(): T = value

    operator fun invoke(input: String) {
        value = parser.parse(input) ?: return
    }

    private fun <T> observe(property: String) = { _: KProperty<*>, _: T, new: T ->
        plugin.config.set(property, new)
        plugin.saveConfig()
    }
}