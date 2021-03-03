package xyz.atrius.waystones.commands

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.pluralize
import xyz.atrius.waystones.utility.translateColors

object GetKeyCommand : SimpleCommand("getkey", "gk", "key", "k") {
    // Calculate Player and Amount Values
    private fun getPlayerAndAmount(default: Player, first: String, second: String): Pair<Player, Int> {
        val player = Bukkit.getPlayer(first) ?: default
        val amount = second.toIntOrNull() ?: first.toIntOrNull() ?: 1
        return player to amount
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        // Set Amount and Player to give WarpKeys to
        val (player, amount) = getPlayerAndAmount(
            sender as Player,
            args.getOrNull(0) ?: "",
            args.getOrNull(1) ?: ""
        )
        // Check Permissions
        if (
            !sender.hasPermission("waystones.getkey.self") || // Give Key to Self
            (!sender.hasPermission("waystones.getkey.all") && sender != player)
        ) { // Give Key to Other
            sender.sendMessage("&cYou don't have permission to reload this!".translateColors())
            return
        }
        // Add WarpKey(s) to Player inventory
        player.inventory.addItem(defaultWarpKey(amount))
        // Inform Player of given WarpKey
        sender.sendMessage("&d[Waystones]&r Gave &b$amount&r WarpKey(s) to &a${player.name}&r"
            .pluralize(amount).translateColors())
    }
}