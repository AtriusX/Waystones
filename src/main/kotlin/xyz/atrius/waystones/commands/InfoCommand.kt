package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.message

object InfoCommand : SimpleCommand("info", "i") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        sender.message("""
            |
            |&7------------- &dWaystones &7------------&r
            |
            |Version&7: &a${plugin.description.version}&r
            |Author&7: &dAtri&r
            |
            |&7-----------------------------------
        """.trimMargin())
    }
}