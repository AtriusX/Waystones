package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.AdvancementManager
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.message

object ReloadCommand : SimpleCommand("reload", "rl") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("waystones.reload"))
            return sender.message(localization["command-no-permission"])
        // TODO: Localize new command messages
        when (args[0].toLowerCase()) {
            in listOf("c", "conf", "config") -> {
                ConfigManager.reload()
                sender.message(localization["command-reload-config"])
            }
            in listOf("a", "adv", "advancements") -> {
                AdvancementManager.reload()
                sender.message(localization["command-reload-advancements"])
            }
            else -> sender.message(localization["command-reload-failed", args[0]])
        }
    }
}
