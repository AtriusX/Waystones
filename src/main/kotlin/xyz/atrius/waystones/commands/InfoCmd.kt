package xyz.atrius.waystones.commands

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

fun infoCmd (
    sender: CommandSender
): Boolean {
    val plugin = Bukkit.getPluginManager().getPlugin("Waystones")
    sender.sendMessage("------------ §dWaystones§r ------------")
    sender.sendMessage("Version: §a${plugin?.description?.version}")
    sender.sendMessage("Author: §dAtriusX§r")
    sender.sendMessage("§aCommands: §r")
    // sender.sendMessage("§a/waystones reload§r - Reloads plugin config")
    sender.sendMessage("§a/waystones warpkey §e[amount] [player]§r - Gives player set amount of keys")
    return true
}