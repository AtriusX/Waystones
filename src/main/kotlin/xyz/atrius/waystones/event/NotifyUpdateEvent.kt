package xyz.atrius.waystones.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.persistence.PersistentDataType
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.service.PluginUpdateService
import xyz.atrius.waystones.utility.message
import xyz.atrius.waystones.utility.toKey

@Single
class NotifyUpdateEvent(
    private val localization: LocalizationManager,
    private val pluginUpdateService: PluginUpdateService,
) : Listener {

    private val dismissalGroup = "dismissed_updates".toKey()

    @EventHandler
    fun onNotifyUpdateEvent(event: PlayerJoinEvent) {
        val player = event.player
        // Avoid calling the update service if the player doesn't have the correct permissions
        if (!player.hasPermission("waystones.admin")) {
            return
        }
        // Retrieve the current latest plugin release
        val version = pluginUpdateService
            .checkForUpdate()
            .getOrNull()
            ?.name
            ?: return
        // Check that the player has any dismissed versions first before checking the contents
        val dismissedVersions = player.persistentDataContainer
            .get(dismissalGroup, PersistentDataType.LIST.strings())
            ?: mutableListOf()
        // Ensure the player has not dismissed the current version name
        if (version in dismissedVersions) {
            logger.debug("Player {} dismissed version update '{}', silencing notification...", player, version)
            return
        }
        // If the player passes all checks, notify them of the update
        player.message(localization["plugin-update-available-login", version])
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(NotifyUpdateEvent::class.java)
    }
}
