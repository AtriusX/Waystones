package xyz.atrius.waystones.manager

import org.bukkit.event.Listener
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.internal.KotlinPlugin

@Single
class EventManager(
    private val plugin: KotlinPlugin,
    private val listeners: List<Listener>,
) {

    fun initialize() {
        val events = plugin.server.pluginManager

        logger.info("Loading ${listeners.size} event subscriber(s)...")

        listeners.forEach {
            events.registerEvents(it, plugin)
        }

        logger.info("Events loaded!")
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(EventManager::class.java)
    }
}
