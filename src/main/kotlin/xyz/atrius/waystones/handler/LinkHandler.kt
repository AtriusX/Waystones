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
            return Fail(localization["link-no-permission"].toString())
        // Prevent linking if relinking is disabled
        val meta = item.itemMeta as CompassMeta
        return if (!configuration.relinkableKeys() && meta.hasLodestone())
            Fail(localization["link-not-relinkable"].toString())
        // Prevent relinking if the location is the same
        else if (!player.immortal && meta.lodestone == block.location)
            Fail(localization["link-already-linked"].toString())
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
        val name = WarpNameService[block.location] ?: localization["unnamed-waystone"]
        lore = listOf(localization["link-key-lore", name, lodestone?.locationCode].toString())
    }
}
