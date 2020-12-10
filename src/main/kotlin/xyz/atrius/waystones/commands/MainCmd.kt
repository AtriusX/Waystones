package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit

object MainCmd : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        lbl: String,
        args: Array<out String>
    ): Boolean {
        // Plugin Info
        if (args.isEmpty()) infoCmd(sender)

        // Register Commands
        else if (args[0] == "warpkey") warpKeyCmd(sender, cmd, lbl, args.drop(1))

        // Command Not Found
        else sender.sendMessage("§d[Waystones]§r Unknown command, check §a/waystones§r")
        return true
    }
}