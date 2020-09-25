package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.CompassMeta
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*

class LinkEvent(private val names : WarpNameService) : Listener {

    @EventHandler
    fun onSet(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return
        val item  = event.item ?: return
        val block = event.clickedBlock ?: return
        if (!item.isWarpKey() || block.type != Material.LODESTONE)
            return
        val player = event.player
        // Prevent linking if relinking is disabled
        val meta = item.itemMeta as CompassMeta
        if (!configuration.relinkableKeys && meta.hasLodestone()) {
            player.sendActionError("The destination for this key has been sealed")
            return event.cancel()
        }
        // Prevent relinking if the location is the same
        if (!player.immortal && meta.lodestone == block.location)
            return event.cancel()
        event.cancel()
        val key = defaultWarpKey().update<CompassMeta> {
            lodestone = block.location
            isLodestoneTracked = true
            lore = listOf(
                "${ChatColor.DARK_PURPLE}${names[block.location] ?: "Warpstone"}: [${block.location.locationCode}]"
            )
        }
        // Add item to players inventory
        player.inventory.addItemNaturally(item, key)
        player.playSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)
    }
}