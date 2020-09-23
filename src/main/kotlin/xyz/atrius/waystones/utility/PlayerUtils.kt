package xyz.atrius.waystones.utility

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffectType

val Player.immortal: Boolean
    get() = gameMode in listOf(GameMode.CREATIVE, GameMode.SPECTATOR)

fun Player.sendActionMessage(message: String, color: ChatColor? = null) =
    this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${color ?: ""}${ChatColor.BOLD}$message"))

fun Player.sendActionError(message: String) =
        sendActionMessage(message, ChatColor.RED)

fun Player.playSound(sound: Sound, volume: Float = 1f, pitch: Float = 1f) =
    playSound(location, sound, volume, pitch)

fun Player.hasPortalSickness() =
    getPotionEffect(PotionEffectType.CONFUSION)?.amplifier ?: 0 >= 4

fun PlayerInventory.hasStackWithRoom(item: ItemStack) =
    any { it?.isSimilar(item) == true && it.amount < it.maxStackSize }

fun PlayerInventory.addItemNaturally(original: ItemStack, new: ItemStack) {
    val player = holder as Player
    when {
        player.immortal ->
            addItem(new)
        original.amount == 1 ->
            if (hasStackWithRoom(new)) {
                addItem(new)
                original.amount--
            } else {
                original.itemMeta = new.itemMeta
            }
        else -> {
            if (hasStackWithRoom(new))
                addItem(new)
            else
                player.world.dropItem(player.location.UP, new)
            original.amount--
        }
    }
}