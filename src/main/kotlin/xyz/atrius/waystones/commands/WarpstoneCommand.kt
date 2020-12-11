package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import xyz.atrius.waystones.utility.defaultWarpKey

object WarpstoneCommand : CommandExecutor {
    // CommandExecutor / Subcommand Manager
    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        lbl: String,
        args: Array<String>
    ): Boolean {
        // Plugin Info
        if (args.isEmpty())
            return infoCmd(sender)

        // Register Give WarpKey
        val giveKeyCommands = arrayOf("getkey", "gk", "warpkey", "wk", "key", "k")
        if (args[0].toLowerCase() in giveKeyCommands)
            return getkeyCmd(sender, args.drop(1))

        // Command Not Found
        else sender.sendMessage("§d[Waystones]§r Unknown command, check §a/waystones§r")
        return true
    }

    // Command - Plugin Info
    private fun infoCmd (sender: CommandSender): Boolean {
        val plugin = Bukkit.getPluginManager().getPlugin("Waystones")
        sender.sendMessage("------------ §dWaystones§r ------------")
        sender.sendMessage("Version: §a${plugin?.description?.version}")
        sender.sendMessage("Author: §dAtriusX§r")
        sender.sendMessage("§aCommands: §r")
        sender.sendMessage("§a/waystones getkey §e[amount] [player]§r - Gives player set amount of keys")
        return true
    }
    // Command - Give WarpKey(s)
    private fun getkeyCmd (
        sender: CommandSender,
        args: List<String>
    ): Boolean {
        // Set Amount and Player to give WarpKeys to
        val amt = args.getOrNull(0)?.toIntOrNull() ?: 1
        val player = args.getOrNull(1)?.let { Bukkit.getServer().getPlayer(it) } ?: sender as Player

        // Check Permissions
        if (
            (sender == player && !sender.hasPermission("waystones.givekey.self")) ||
            (sender != player && !sender.hasPermission("waystones.givekey.others"))
        ) {
            sender.sendMessage("§d[Waystones]§r §cYou don't have permission to run this command§r")
            return true
        }

        // Add WarpKey to Player inventory
        val keyStack = defaultWarpKey()
        keyStack.amount = amt
        player.inventory.addItem(keyStack)

        // Inform Player of given WarpKey
        sender.sendMessage("§d[Waystones]§r Gave §a$amt§r WarpKey to §a${player.name}§r")
        return true
    }
}