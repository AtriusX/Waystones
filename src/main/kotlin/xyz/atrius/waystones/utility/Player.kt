package xyz.atrius.waystones.utility

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.atrius.waystones.data.config.LocalizedString

val Player.immortal: Boolean
    get() = gameMode in listOf(GameMode.CREATIVE, GameMode.SPECTATOR)

fun Player.sendActionMessage(message: LocalizedString?) = when (message) {
    null -> Unit
    else -> sendActionMessage(message.toString())
}

fun Player.sendActionMessage(message: String?) = when (message) {
    null -> Unit
    else -> sendActionBar { Component.text(message) }
}

fun Player.sendActionError(message: LocalizedString?) = when (message) {
    null -> Unit
    else -> sendActionError(message.toString())
}

fun Player.sendActionError(message: String?) = when (message) {
    null -> Unit
    else -> sendActionBar {
        val style = Style
            .style()
            .color(TextColor.color(255, 0, 0))
            .decorate(TextDecoration.BOLD)

        Component
            .text(message)
            .style(style)
    }
}

fun Player.playSound(sound: Sound, volume: Float = 1f, pitch: Float = 1f) =
    playSound(location, sound, volume, pitch)

fun Player.hasPortalSickness() =
    (getPotionEffect(PotionEffectType.NAUSEA)?.amplifier ?: 0) >= 4

fun Player.addPotionEffects(vararg effects: PotionEffect) =
    addPotionEffects(arrayListOf(*effects))

fun CommandSender.message(message: LocalizedString, colorCode: Char = '&') =
    message(message.toString(), colorCode)

fun CommandSender.message(message: String, colorCode: Char = '&') =
    sendMessage(message.translateColors(colorCode))

