package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.atrius.waystones.utility.defaultWarpKey
import org.bukkit.Bukkit

object WarpKeyCmd : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        lbl: String,
        args: Array<out String>
    ): Boolean {
        // Amount of WarpKeys to give the Player
        var amt = 1
        if (args.isNotEmpty()) {
            // Check if amount argument is strictly numerical
            if (args[0].toIntOrNull() == null) {
                sender.sendMessage("§5[Waystones]§r Amount must be numerical")
                return false
            } else amt = Integer.parseInt(args[0])
        }
        // Player to give WarpKeys to
        val player =
            if (args.size >= 2) Bukkit.getServer().getPlayer(args[1])
            else (sender as Player)
        // Add WarpKey to Player inventory
        for (i in 1..amt) player?.inventory?.addItem(defaultWarpKey())
        // Inform Player of given WarpKey
        sender.sendMessage("§5[Waystones]§r Gave §a$amt§r WarpKey to §a${sender.name}§r")
        return true
    }
}