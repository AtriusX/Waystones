package xyz.atrius.waystones.command.waystones

import arrow.core.merge
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.literal
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.service.PluginUpdateService
import xyz.atrius.waystones.service.PluginUpdateService.UpdateCheckError
import xyz.atrius.waystones.utility.message
import xyz.atrius.waystones.utility.toKey

@Single
class UpdateCommand(
    private val pluginUpdateService: PluginUpdateService,
    private val localization: LocalizationManager,
) : WaystoneSubcommand {
    override val name: String = "update"
    override val basePermission: String = "waystones.admin"

    private val dismissalGroup = "dismissed_updates".toKey()

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> = base
        .then(
            literal("check")
                .executes { checkCommand(it.source.sender) }
        )
        .then(
            literal("dismiss")
                .requires { it.sender is Player }
                .executes { dismissCommand(it.source.sender) }
        )

    private fun checkCommand(sender: CommandSender): Int {
        val result = pluginUpdateService
            .checkForUpdate()
            .mapLeft { exchangeError(it) }
            .map { localization["plugin-update-available", it.name] }
            .merge()

        sender.message(result)
        return Command.SINGLE_SUCCESS
    }

    private fun dismissCommand(sender: CommandSender): Int {
        // We provide a check for this already in the 'requires' block we call
        // this function with, but we should validate it here too for safety
        val player = sender as? Player
            ?: return Command.SINGLE_SUCCESS
        // Check if update is available first
        val version = pluginUpdateService
            .checkForUpdate()
            .getOrNull()
            ?.name
        // Ensure an update exists before attempting to dismiss it
        if (version == null) {
            player.message(localization["no-plugin-update-available"])
            return Command.SINGLE_SUCCESS
        }
        // Players have a PDC we can store items in, so lets store a list of all
        // versions a player has dismissed
        val pdc = player.persistentDataContainer
        val items = pdc
            .get(dismissalGroup, PersistentDataType.LIST.strings())
            ?: mutableListOf()
        // We should only store new items if there is a new version, so avoid
        // anything that comes back on the left path
        // Store the current version inside the PDC and save it
        items.add(version)
        pdc.set(dismissalGroup, PersistentDataType.LIST.strings(), items)
        sender.message(localization["plugin-update-dismissed", version])
        return Command.SINGLE_SUCCESS
    }

    private fun exchangeError(error: UpdateCheckError) = when (error) {
        UpdateCheckError.NoUpdateAvailable -> localization["no-plugin-update-available"]
        UpdateCheckError.UnableToRetrieve -> localization["plugin-update-check-failed"]
    }
}
