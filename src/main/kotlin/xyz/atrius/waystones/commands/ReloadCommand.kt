package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.message

object ReloadCommand : SimpleCommand("reload", "rl") {

    override fun execute(sender: CommandSender, args: Array<String>, flags: Flags) {
        if (!sender.hasPermission("waystones.reload")) {
            sender.message(localization["command-no-permission"])
            return
        }

        ConfigManager.reload()
        sender.message(localization["command-reload"])
    }
}
