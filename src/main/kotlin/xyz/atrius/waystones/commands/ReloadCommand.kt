package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.utility.message

object ReloadCommand : SimpleCommand("reload", "rl") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("waystones.reload")) {
            sender.message("&7[&dWaystones&7] &cYou don't have permission to reload this!")
            return
        }

        sender.message("&7[&dWaystones&7] &aReloaded config!")
        ConfigManager.reload()
    }
}