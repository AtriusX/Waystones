package xyz.atrius.waystones.commands

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.message

object GetKeyCommand : SimpleCommand("getkey", "gk", "key", "k") {
    // Calculate Player and Amount Values
    private fun getPlayerAndAmount(default: Player?, first: String?, second: String?): Pair<Player?, Int> {
        val player = if (first != null)
            Bukkit.getPlayer(first) ?: default else default
        val amount = second?.toIntOrNull() ?: first?.toIntOrNull() ?: 1
        return player to amount
    }

    override fun execute(sender: CommandSender, args: Array<String>, flags: Flags) {
        // Set Amount and Player to give WarpKeys to
        val (player, amount) = getPlayerAndAmount(
            sender as? Player,
            args.getOrNull(0),
            args.getOrNull(1)
        )
        // Return if selected player does not exist
        player ?: return sender.message(localization["command-bad-sender", "console"])
        // Check Permissions
        if (!sender.hasPermission("waystones.getkey.self")
            || !sender.hasPermission("waystones.getkey.all")
            && sender != player
        ) return sender.message(localization["command-no-permission"])
        // Add WarpKey(s) to Player inventory
        player.inventory.addItem(defaultWarpKey(amount))
        // Inform Player of given WarpKey
        sender.message(localization["command-give-key", amount, player.displayName])
    }
}
