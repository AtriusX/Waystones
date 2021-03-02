package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.MultiReferencable

class CommandNamespace(
    override vararg val aliases: String
) : CommandExecutor, MultiReferencable {

    private val commands: ArrayList<SimpleCommand> = arrayListOf()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = true.also {
        for (c in commands) {
            if (args[0] in c.aliases) {
                c.execute(sender, args.drop(1).toTypedArray())
                break
            }
        }
    }

    fun register(vararg command: SimpleCommand): CommandNamespace {
        commands += command
        return this
    }
}