package xyz.atrius.waystones.handler

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.handler.HandleState.*
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*

class LinkHandler(
        override val player: Player,
        private val item: ItemStack,
        private val block: Block
) : PlayerHandler {

    override fun handle(): HandleState {
        if (!item.isWarpKey() || block.type != Material.LODESTONE)
            return Ignore
        // Check if the player is able to link to this waystone
        if (!player.hasPermission("waystones.link"))
            return Fail(localization.localize("link-no-permission"))
        // Prevent linking if relinking is disabled
        val meta = item.itemMeta as CompassMeta
        return if (!configuration.relinkableKeys() && meta.hasLodestone())
            Fail(localization.localize("link-not-relinkable"))
        // Prevent relinking if the location is the same
        else if (!player.immortal && meta.lodestone == block.location)
            Fail(localization.localize("link-already-linked"))
        else Success
    }

    fun link() {
        // Add item to players inventory
        player.inventory.addItemNaturally(item, defaultWarpKey().link(block))
        player.playSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)
    }

    private fun ItemStack.link(block: Block) = update<CompassMeta> {
        lodestone = block.location
        isLodestoneTracked = true
        val name = WarpNameService[block.location] ?: localization.localize("unnamed-waystone")
        lore = listOf(localization.localize("link-key-lore", name, lodestone?.locationCode))
    }
}
