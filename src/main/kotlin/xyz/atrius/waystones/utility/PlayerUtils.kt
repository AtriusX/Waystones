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
import xyz.atrius.waystones.SicknessOption
import xyz.atrius.waystones.configuration

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
        // Add item to inventory
        player.immortal -> {
            addItem(new)
        }
        // Update item in slot if only one exists
        original.amount == 1 && original.type == new.type -> {
            original.itemMeta = new.itemMeta
        }
        // Add the item to the first similar stack or drop it
        else -> {
            if (!hasStackWithRoom(new))
                player.world.dropItem(player.location.UP, new)
            else addItem(new)
            original.amount--
        }
    }
}

fun Player.canWarp(): Boolean =
    configuration.portalSickWarping == SicknessOption.PREVENT_TELEPORT && hasPortalSickness()