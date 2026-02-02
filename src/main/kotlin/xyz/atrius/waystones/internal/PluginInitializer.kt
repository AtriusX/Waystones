package xyz.atrius.waystones.internal

interface PluginInitializer {

    fun enable(plugin: KotlinPlugin)

    fun disable(plugin: KotlinPlugin)
}
