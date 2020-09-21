package xyz.atrius.waystones.utility

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

fun Player.sendActionMessage(message: String, color: String = "#ffffff") =
    this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))

fun Player.sendActionError(message: String) =
        sendActionMessage(message, "#ff0000")
