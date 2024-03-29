package xyz.atrius.waystones.data.config

import xyz.atrius.waystones.data.Property
import xyz.atrius.waystones.plugin
import kotlin.collections.MutableMap.MutableEntry

object ConfigManager {
    private val options = hashMapOf<String, Property<*>>()

    internal fun register(key: String, ref: Property<*>) {
        if (key !in options) options[key] = ref
    }

    @Suppress("UNCHECKED_CAST")
    operator fun get(option: String?): Property<*>? = options[option]

    operator fun set(option: String?, value: String?): Boolean {
        return options[option]?.invoke(value) ?: false
    }

    operator fun iterator(): MutableIterator<MutableEntry<String, Property<*>>> {
        return options.iterator()
    }

    fun getOptions(): Set<String> =
        options.keys

    fun reload() {
        plugin.reloadConfig()
        for ((prop, option) in options) {
            option(plugin.config.get(prop))
        }
    }
}