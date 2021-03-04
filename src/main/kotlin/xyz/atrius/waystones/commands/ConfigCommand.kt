package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.utility.message

object ConfigCommand : SimpleCommand("config", "conf", "co", "c") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        val setting = args.getOrNull(0)
        val new = args.getOrNull(1)
        // List all arguments if the argument list is empty
        if (args.isEmpty())
            return listOptions(sender)
        // Return the previous config value
        if (new == null) {
            sender.message("&6$setting&f: &b${ConfigManager[setting]?.invoke()}")
            return
        }
        // Assign the new config value to the system and print result of action
        if (ConfigManager[setting]?.invoke(new) == true) {
            sender.message("&bSuccessfully updated &c$setting &bto &c$new!")
        } else {
            sender.message("&cFailed to update setting $setting.")
        }
    }

    private fun listOptions(sender: CommandSender) {
        sender.message("&6------------ &cConfig&6 ------------")
        // Display each config property
        for ((prop, value) in ConfigManager)
            sender.message("&6$prop&f: &b${value()}")
        sender.message("&6-----------------------------------")
    }
}