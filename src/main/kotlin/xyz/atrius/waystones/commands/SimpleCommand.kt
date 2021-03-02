package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.MultiReferencable

abstract class SimpleCommand(override vararg val aliases: String) : MultiReferencable {

    abstract fun execute(sender: CommandSender, args: Array<String>)
}
