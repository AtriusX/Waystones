package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.message

object InfoCommand : SimpleCommand("info", "i") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        sender.message("""
            |------------ &dWaystones&r ------------
            |Version: &a${plugin.description.version}&r
            |Author: &dAtri&r
            |-----------------------------------
        """.trimMargin())
    }
}