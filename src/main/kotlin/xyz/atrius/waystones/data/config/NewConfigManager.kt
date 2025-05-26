package xyz.atrius.waystones.data.config

import org.koin.core.annotation.Single
import xyz.atrius.waystones.internal.KotlinPlugin
import java.util.*

@Single
class NewConfigManager(
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

    fun <T : Any> getValueOrNull(option: String?): T? =
        getPropertyOrNull<T>(option)?.value

    fun <T : Any> getListValueOrNull(option: String?): List<T>? =
        getListPropertyOrNull<T>(option)?.value

    fun <T : Any> setProperty(option: String?, value: T): Boolean {
        val prop = getPropertyOrNull<T>(option)

        if (prop == null) {
            return false
        }

        prop.update(value)
        plugin.config.set(
            prop.property, when (value) {
                is Enum<*> -> value.name
                is Locale -> value.toString()
                else -> value
            }
        )

        plugin.saveConfig()
        return true
    }

    fun <T : Any> setProperty(option: String?, value: List<T>): Boolean {
        val prop = getListPropertyOrNull<T>(option)

        if (prop == null) {
            return false
        }

        prop.update(value)
        plugin.config.set(prop.property,
            (value as List<*>)
                .map(Any?::toString)
        )
        plugin.saveConfig()
        return true
    }

    fun reload() {
        plugin.reloadConfig()

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

    operator fun iterator(): Iterator<Map.Entry<String, ConfigPropertyBase<*, *, *>>> =
        options.iterator()
}