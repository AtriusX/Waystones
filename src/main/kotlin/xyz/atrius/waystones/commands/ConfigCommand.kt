package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.utility.message

object ConfigCommand : SimpleCommand("config", "conf", "co", "c") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        // Permissions Check
        if (!sender.hasPermission("waystones.config")) {
            sender.message("&7[&dWaystones&7] &cYou don't have permission to run this!")
            return
        }

        val setting = args.getOrNull(0)
        val new = args.getOrNull(1)
        // List all arguments if the argument list is empty
        if (args.isEmpty())
            return listOptions(sender)
        // Return the previous config value
        if (new == null) {
            sender.message("&7[&dWaystones&7] &f$setting&7: &b${ConfigManager[setting]?.invoke()}")
            return
        }
        // Assign the new config value to the system and print result of action
        if (ConfigManager[setting]?.invoke(new) == true) {
            sender.message("&7[&dWaystones&7] &bSuccessfully updated &e$setting &bto &a$new!")
        } else {
            sender.message("&7[&dWaystones&7] &cFailed to update setting $setting.")
        }
    }

    private fun listOptions(sender: CommandSender) {
        sender.message("&7--------- &dWaystones Config &7----------&r")
        // Display each config property
        for ((prop, value) in ConfigManager)
            sender.message("&f$prop&f: &b${value()}")
        sender.message("&7-----------------------------------")
    }
}