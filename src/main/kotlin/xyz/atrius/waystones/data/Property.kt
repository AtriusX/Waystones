package xyz.atrius.waystones.data

import org.bukkit.configuration.file.FileConfiguration
import xyz.atrius.waystones.data.config.Parser
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.plugin
import java.util.*
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class Property<T>(
    property: String,
    default: T,
    private val parser: Parser<T>,
    private val onUpdate: () -> Unit = {}
) {
    private val config: FileConfiguration
        get() = plugin.config

    private var value: T by observable(
        parser.parse(config.get(property))
            ?: default.also { update(property, it) },
        observe(property)
    )

    init {
        ConfigManager.register(property, this)
    }

    operator fun invoke(): T = value

    operator fun invoke(input: Any?): Boolean {
        value = parser.parse(input) ?: return false
        onUpdate()
        return true
    }

    private fun <T> observe(property: String) = { _: KProperty<*>, _: T, new: T ->
        update(property, new)
    }

    private fun <T> update(property: String, new: T) {
        config.set(property, when (new) {
            is Enum<*> -> new.name
            is Locale -> new.toString()
            else -> new
        })
        plugin.saveConfig()
    }

    override fun toString(): String =
        parser.toString(value)
}
