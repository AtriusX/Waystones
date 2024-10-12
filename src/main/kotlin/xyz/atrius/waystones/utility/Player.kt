package xyz.atrius.waystones.utility

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.atrius.waystones.SicknessOption.PREVENT_TELEPORT
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.advancement.Advancement
import xyz.atrius.waystones.data.config.LocalizedString
import xyz.atrius.waystones.handler.HandleState
import org.bukkit.advancement.Advancement as SpigotAdvancement

val Player.immortal: Boolean
    get() = gameMode in listOf(GameMode.CREATIVE, GameMode.SPECTATOR)

fun Player.sendActionMessage(message: LocalizedString?) = if (message != null)
    sendActionMessage(message.toString())
else Unit

fun Player.sendActionMessage(message: String?) = if (message != null)
    spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
else Unit

fun Player.sendActionError(message: LocalizedString?) = if (message != null)
    sendActionError(message.toString())
else Unit

fun Player.sendActionError(message: String?) = if (message != null)
    sendActionMessage("${ChatColor.RED}${ChatColor.BOLD}$message")
else Unit

fun Player.sendActionError(fail: HandleState.Fail) =
    sendActionError(fail.error)

fun Player.clearActionMessage() =
    sendActionMessage("")

fun Player.playSound(sound: Sound, volume: Float = 1f, pitch: Float = 1f) =
    playSound(location, sound, volume, pitch)

fun Player.hasPortalSickness() =
    (getPotionEffect(PotionEffectType.NAUSEA)?.amplifier ?: 0) >= 4

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
            if (addItem(new).isNotEmpty())
                player.world.dropItem(player.location.UP, new)
            original.amount--
        }
    }
}

fun Player.canWarp(): Boolean =
    configuration.portalSickWarping() == PREVENT_TELEPORT && hasPortalSickness()

fun Player.addPotionEffects(vararg effects: PotionEffect) =
    addPotionEffects(arrayListOf(*effects))

fun CommandSender.message(message: LocalizedString, colorCode: Char = '&') =
    message(message.toString(), colorCode)

fun CommandSender.message(message: String, colorCode: Char = '&') =
    sendMessage(message.translateColors(colorCode))

fun Player.awardAdvancement(adv: Pair<String, Advancement>) = awardAdvancement(adv.second)

fun Player.awardAdvancement(adv: Advancement) = awardAdvancement(adv.toInstance())

fun Player.awardAdvancement(adv: SpigotAdvancement?) {
    if (adv == null || !configuration.advancements())
        return
    val criteria = getAdvancementProgress(adv)
    criteria.remainingCriteria
        .forEach(criteria::awardCriteria)
}
