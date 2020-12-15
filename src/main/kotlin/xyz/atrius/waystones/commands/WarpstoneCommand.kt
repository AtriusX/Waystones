package xyz.atrius.waystones.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.pluralize
import xyz.atrius.waystones.utility.translateColors

object WarpstoneCommand : CommandExecutor {
    // CommandExecutor / Subcommand Manager
    override fun onCommand(
        sender: CommandSender, command: Command, label: String, args: Array<String>
    ): Boolean {
        // Plugin Info
        if (args.isEmpty())
            return infoCommand(sender)
        // Register Give WarpKey
        val aliases = arrayOf("getkey", "gk", "warpkey", "wk", "key", "k")
        if (args[0].toLowerCase() in aliases)
            return getKeyCommand(sender, args.drop(1))
        // Command Not Found
        else sender.sendMessage("&d[Waystones]&r Unknown command, check &a/waystones&r".translateColors('&'))
        return true
    }

    // Send Permission Error
    private fun sendPermError(sender: CommandSender) = true.also {
        sender.sendMessage("&d[Waystones]&r &cYou don't have permission to run this command&r"
            .translateColors('&'))
    }

    // Calculate Player and Amount Values
    private fun getPlayerAndAmount(default: Player, first: String, second: String): Pair<Player, Int> {
        val player = Bukkit.getPlayer(first) ?: default
        val amount = second.toIntOrNull() ?: first.toIntOrNull() ?: 1
        return player to amount
    }

    // Command - Plugin Info
    private fun infoCommand(sender: CommandSender) = true.also {
        sender.sendMessage("""
            ------------ &dWaystones&r ------------
            Version: &a${plugin.description.version}&r
            Author: &dAtriusX&r
            &aCommands: &r
            &a/waystones getkey &b[ &ecount &b| &eplayer &b[&ecount&b] ]&r - Gives player set amount of keys
        """.trimIndent().translateColors('&'))
    }

    // Command - Give WarpKey(s)
    private fun getKeyCommand(sender: CommandSender, args: List<String>): Boolean {
        // Set Amount and Player to give WarpKeys to
        val (player, amount) = getPlayerAndAmount(sender as Player, args.getOrNull(0) ?: "", args.getOrNull(1) ?: "")
        // Check Permissions
        if (!sender.hasPermission("waystones.getkey.self") || // Give Key to Self
            (!sender.hasPermission("waystones.getkey.all") && sender != player)) // Give Key to Other
            return sendPermError(sender)
        // Add WarpKey(s) to Player inventory
        player.inventory.addItem(defaultWarpKey(amount))
        // Inform Player of given WarpKey
        sender.sendMessage("&d[Waystones]&r Gave &b$amount&r WarpKey(s) to &a${player.name}&r"
            .pluralize(amount).translateColors('&'))
        return true
    }
}