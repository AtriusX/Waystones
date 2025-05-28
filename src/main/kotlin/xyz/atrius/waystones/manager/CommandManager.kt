package xyz.atrius.waystones.manager

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.command.BaseCommand
import xyz.atrius.waystones.internal.KotlinPlugin

@Single
class CommandManager(
    private val plugin: KotlinPlugin,
    private val commands: List<BaseCommand<*>>,
) {

    fun register() {
        val register = plugin.lifecycleManager

        register.registerEventHandler(LifecycleEvents.COMMANDS) {
            // Register all system commands
            for (command in commands) {
                val command = command.create()

                logger.info("Loading command ${command.name}")
                it.registrar().register(command)
            }
        }

        logger.info("Commands loaded!")
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(CommandManager::class.java)
    }
}