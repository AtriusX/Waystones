package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.utility.translateColors

object ReloadCommand : SimpleCommand("reload", "rl") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("waystones.reload")) {
            sender.sendMessage("&cYou don't have permission to reload this!".translateColors())
            return
        }

        sender.sendMessage("&6Reloaded waystones config!".translateColors())
        ConfigManager.reload()
    }
}