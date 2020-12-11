package xyz.atrius.waystones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.atrius.waystones.utility.defaultWarpKey
import org.bukkit.Bukkit

fun warpKeyCmd (
    sender: CommandSender,
    cmd: Command,
    lbl: String,
    args: List<String>
): Boolean {
    // Permission Check
    if (!sender.hasPermission("waystones.warpkey")) {
        sender.sendMessage("§d[Waystones]§r §cYou don't have permission to run this command§r")
        return true
    }

    // Set Amount and Player to give WarpKeys to
    val amt = args.getOrNull(0)?.toIntOrNull() ?: 1
    val player = args.getOrNull(1)?.let { Bukkit.getServer().getPlayer(it) } ?: sender as Player

    // Add WarpKey to Player inventory
    for (i in 1..amt) player.inventory.addItem(defaultWarpKey())

    // Inform Player of given WarpKey
    sender.sendMessage("§d[Waystones]§r Gave §a$amt§r WarpKey to §a${sender.name}§r")
    return true
}