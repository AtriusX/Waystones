package xyz.atrius.waystones.data.config

import xyz.atrius.waystones.data.Property

object ConfigManager {
    private val options = hashMapOf<String, Property<*>>()

    fun register(key: String, ref: Property<*>) {
        options[key] = ref
    }

    operator fun get(option: String): Any? =
        options[option]?.invoke()

    operator fun set(option: String, value: String) {
        options[option]?.invoke(value)
    }

    fun getOptions(): Set<String> =
        options.keys
}