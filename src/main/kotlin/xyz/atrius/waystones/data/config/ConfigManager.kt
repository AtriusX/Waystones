package xyz.atrius.waystones.data.config

import xyz.atrius.waystones.data.Property
import xyz.atrius.waystones.plugin

object ConfigManager {
    private val options = hashMapOf<String, Property<*>>()

    internal fun register(key: String, ref: Property<*>) {
        if (key !in options) options[key] = ref
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(option: String): T? =
        options[option]?.invoke() as T?

    operator fun set(option: String, value: String) {
        options[option]?.invoke(value)
    }

    fun getOptions(): Set<String> =
        options.keys

    fun reload() {
        for ((prop, option) in options) {
            option(plugin.config.getString(prop))
        }
    }
}