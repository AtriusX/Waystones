package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.atrius.waystones.data.config.ConfigManager
import xyz.atrius.waystones.utility.translateColors

interface MultiReferencable {

    val aliases: Array<out String>
}

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

abstract class SimpleCommand(override vararg val aliases: String) : MultiReferencable {

    abstract fun execute(sender: CommandSender, args: Array<String>)
}

object ReloadCommand : SimpleCommand("reload", "rl") {

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("waystones.reload")) {
            sender.sendMessage("&cYou don't have permission to reload this!".translateColors())
            return
        }

        sender.sendMessage("&6Reloaded waystones config!".translateColors())
        ConfigManager.reload()
    }
}