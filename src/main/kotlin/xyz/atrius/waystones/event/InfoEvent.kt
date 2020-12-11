package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*

class InfoEvent(private val names : WarpNameService) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        // Filter non left-click events
        if (event.action != Action.LEFT_CLICK_BLOCK)
            return
        val block  = event.clickedBlock
        val player = event.player
        val item   = player.inventory.itemInMainHand
        // Make sure the correct block/item pair is used
        if (block?.type != Material.LODESTONE || !item.isWarpKey())
            return
        val name = names[block.location] ?: "None"
        val state = block.getWarpState(player)
        // Skip any non-warp blocks
        if (state == WarpState.None)
            return
        player.sendActionMessage("Name: $name | $state", ChatColor.DARK_AQUA)
        event.cancel()
    }
}