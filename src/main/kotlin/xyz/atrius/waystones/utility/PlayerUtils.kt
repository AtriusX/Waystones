package xyz.atrius.waystones.utility

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player

val Player.immortal: Boolean
    get() = gameMode in listOf(GameMode.CREATIVE, GameMode.SPECTATOR)

fun Player.sendActionMessage(message: String, color: ChatColor? = null) =
    this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${color ?: ""}${ChatColor.BOLD}$message"))

fun Player.sendActionError(message: String) =
        sendActionMessage(message, ChatColor.RED)

fun Player.playSound(sound: Sound, volume: Float = 1f, pitch: Float = 1f) =
    playSound(location, sound, volume, pitch)