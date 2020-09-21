package xyz.atrius.waystones.utility

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import java.awt.Color

fun Player.sendActionMessage(message: String, color: Color = Color.white) =
    this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.of(color)}$message"))

fun Player.sendActionError(message: String) =
        sendActionMessage(message, Color.RED)
