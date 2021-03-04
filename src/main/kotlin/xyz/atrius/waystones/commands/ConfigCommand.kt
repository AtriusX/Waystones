package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.utility.translateColors

object ConfigCommand : SimpleCommand("config", "conf", "co", "c") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        val setting = args.getOrNull(0)
        val new = args.getOrNull(1)
        // List all arguments if the argument list is empty
        if (args.isEmpty()) {
            listOptions(sender)
            return
        }
        // Return the previous config value
        if (new == null) {
            sender.sendMessage("&6$setting&f: &b${ConfigManager[setting]?.invoke()}".translateColors())
            return
        }
        // Assign the new config value to the system and print result of action
        if (ConfigManager[setting]?.invoke(new) == true) {
            sender.sendMessage("&bSuccessfully updated &c$setting &bto &c$new!".translateColors())
        } else {
            sender.sendMessage("&cFailed to update setting $setting.".translateColors())
        }
    }

    private fun listOptions(sender: CommandSender) {
        sender.sendMessage("&6------------ &Config&r ------------".translateColors())
        // Display each config property
        for ((prop, value) in ConfigManager)
            sender.sendMessage("&6$prop&f: &b${value()}".translateColors())
        sender.sendMessage("&6-----------------------------------".translateColors())
    }
}