package xyz.atrius.waystones.utility

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Sound
import org.bukkit.entity.Player

fun Player.sendActionMessage(message: String, color: String = "#ffffff") =
    this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.AQUA}$message")).also { println(ChatColor.AQUA.toString()) }

fun Player.sendActionError(message: String) =
        sendActionMessage(message, "#ff0000")

fun Player.playSound(sound: Sound, volume: Float = 1f, pitch: Float = 1f) =
    playSound(location, sound, volume, pitch)