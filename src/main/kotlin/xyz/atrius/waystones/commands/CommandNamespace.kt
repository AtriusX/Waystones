package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.MultiReferencable
import xyz.atrius.waystones.utility.message
import xyz.atrius.waystones.utility.split

class CommandNamespace(
    override vararg val aliases: String
) : CommandExecutor, MultiReferencable {

    private val commands: ArrayList<SimpleCommand> = arrayListOf()

    init {
        require(aliases.isNotEmpty()) {
            "A single alias is required."
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (args.isEmpty()) {
            listCommands(sender)
            return true
        }
        // Execute the given command
        for (c in commands) {
            if (args[0] in c.aliases) {
                val (arguments, flags) = args.drop(1).split { !it.startsWith("-") }
                c.execute(
                    sender,
                    arguments.map(String::lowercase).toTypedArray(),
                    Flags(flags.toSet())
                )
                return true
            }
        }
        sender.message("&7[&dWaystones&7] &cUnknown command, check &a/waystones&r")
        return true
    }

    fun register(vararg command: SimpleCommand): CommandNamespace = also {
        commands += command
    }

    private fun listCommands(sender: CommandSender) {
        sender.message("&7------------ &dWaystones&7 -------------")
        for (command in commands)
            sender.message("&f/${aliases[0]} &b${command.aliases[0]}")
        sender.message("&7-----------------------------------")
    }
}