package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender

fun infoCmd (
    sender: CommandSender
): Boolean {
    sender.sendMessage("------------ §dWaystones§r ------------")
    sender.sendMessage("Author: §dAtriusX§r")
    sender.sendMessage("§aCommands: §r")
    sender.sendMessage("§a/waystones reload§r - Reloads plugin config")
    sender.sendMessage("§a/waystones warpkey §e[amount] [player]§r - Gives player set amount of keys")
    return true
}