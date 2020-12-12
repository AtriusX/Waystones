package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.translateColors
import xyz.atrius.waystones.utility.pluralize

object WarpstoneCommand : CommandExecutor {
    // CommandExecutor / Subcommand Manager
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        // Plugin Info
        if (args.isEmpty())
            return infoCmd(sender)

        // Register Give WarpKey
        val getkeyAliases = arrayOf("getkey", "gk", "warpkey", "wk", "key", "k")
        if (args[0].toLowerCase() in getkeyAliases)
            return getkeyCmd(sender, args.drop(1))

        // Command Not Found
        else sender.sendMessage("&d[Waystones]&r Unknown command, check &a/waystones&r".translateColors('&'))
        return true
    }

    // Command - Plugin Info
    private fun infoCmd (sender: CommandSender): Boolean {
        sender.sendMessage("""
            ------------ &dWaystones&r ------------
            Version: &a${plugin.description.version}&r
            Author: &dAtriusX&r
            &aCommands: &r
            &a/waystones getkey &e[amount] [player]&r - Gives player set amount of keys
        """.trimIndent().translateColors('&'))
        return true
    }
    // Command - Give WarpKey(s)
    private fun getkeyCmd (
        sender: CommandSender,
        args: List<String>
    ): Boolean {
        // Set Amount and Player to give WarpKeys to
        val amount = args.getOrNull(0)?.toIntOrNull() ?: 1
        val player = args.getOrNull(1)?.let { Bukkit.getServer().getPlayer(it) } ?: sender as Player

        // Check Permissions
        if (
            (sender == player && !sender.hasPermission("waystones.getkey.self")) ||
            (sender != player && !sender.hasPermission("waystones.getkey.others"))
        ) {
            sender.sendMessage("&d[Waystones]&r &cYou don't have permission to run this command&r".translateColors('&'))
            return true
        }

        // Add WarpKey(s) to Player inventory
        player.inventory.addItem(defaultWarpKey(amount))

        // Inform Player of given WarpKey
        sender.sendMessage(
            "&d[Waystones]&r Gave &a$amount&r WarpKey(s) to &a${player.name}&r"
                .pluralize(amount).translateColors('&')
        )
        return true
    }
}