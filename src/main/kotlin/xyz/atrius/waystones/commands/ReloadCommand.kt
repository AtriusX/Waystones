package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.message

object ReloadCommand : SimpleCommand("reload", "rl") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("waystones.reload")) {
            sender.message(localization.localize("command-no-permission"))
            return
        }

        ConfigManager.reload()
        localization.reload(configuration.localization())
        sender.message(localization.localize("command-reload"))
    }
}
