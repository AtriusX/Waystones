package xyz.atrius.waystones.manager

import org.bukkit.event.Listener
import org.koin.core.annotation.Single
import xyz.atrius.waystones.internal.KotlinPlugin

@Single
class EventManager(
    private val plugin: KotlinPlugin,
    private val listeners: List<Listener>,
) {

    fun initialize() {
        val events = plugin.server.pluginManager

        listeners.forEach {
            events.registerEvents(it, plugin)
        }
    }
}