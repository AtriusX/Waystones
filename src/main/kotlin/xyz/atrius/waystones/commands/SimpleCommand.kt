package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.MultiReferencable

abstract class SimpleCommand(
    final override vararg val aliases: String
) : MultiReferencable {

    init {
        require(aliases.isNotEmpty()) {
            "A single alias is required."
        }
    }

    abstract fun execute(sender: CommandSender, args: Array<String>, flags: Flags)
}
