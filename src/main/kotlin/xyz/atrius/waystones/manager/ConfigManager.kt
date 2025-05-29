package xyz.atrius.waystones.manager

import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty
import xyz.atrius.waystones.data.config.ConfigPropertyBase
import xyz.atrius.waystones.data.config.ListConfigProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import java.util.Locale

@Single
class ConfigManager(
    val plugin: KotlinPlugin,
    properties: List<ConfigProperty<*>>,
    listProperties: List<ListConfigProperty<*>>,
) {
    private val options: Map<String, ConfigPropertyBase<*, *, *>> = (properties + listProperties)
        .associateBy { it.property }
        .toSortedMap()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getPropertyOrNull(option: String?): ConfigProperty<T>? =
        options[option] as? ConfigProperty<T>

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getListPropertyOrNull(option: String?): ListConfigProperty<T>? =
        options[option] as? ListConfigProperty<T>

    fun <T : Any> getValueOrNull(option: String?): ConfigProperty<T>? =
        getPropertyOrNull(option)

    fun <T : Any> getListValueOrNull(option: String?): ListConfigProperty<T>? =
        getListPropertyOrNull(option)

    fun <T : Any> setProperty(property: String?, value: T): ConfigProperty<T>? {
        val prop = getPropertyOrNull<T>(property)
            ?: return null
        setProperty(prop, value)
        return prop
    }

    fun <T : Any> setProperty(property: String?, value: List<T>): ListConfigProperty<T>? {
        val prop = getListPropertyOrNull<T>(property)
            ?: return null
        setProperty(prop, value)
        return prop
    }

    private fun <T : Any, D : Any, U> setProperty(
        property: ConfigPropertyBase<T, D, U>,
        value: U,
    ): Boolean {
        property.update(value)
        plugin.config.set(
            property.property,
            when (value) {
                is Enum<*> -> value.name
                is Locale -> value.toString()
                else -> value
            }
        )

        plugin.saveConfig()
        return true
    }

    fun load() {
        for ((prop, option) in options) {
            when (option) {
                is ConfigProperty<*> -> {
                    val value = plugin.config.get(prop)
                    option.update(value)
                }

                is ListConfigProperty<*> -> {
                    val value = plugin.config
                        .getList(prop)
                        ?: listOf()
                    option.update(value)
                }
            }
        }
    }

    fun reload() {
        plugin.reloadConfig()
        load()
    }

    operator fun iterator(): Iterator<Map.Entry<String, ConfigPropertyBase<*, *, *>>> =
        options.iterator()
}
