package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.WarpErrorState
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.getWarpState
import xyz.atrius.waystones.utility.isWarpKey
import xyz.atrius.waystones.utility.sendActionMessage

@Single
class InfoEvent(
    private val localization: Localization,
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onClick(event: PlayerInteractEvent) {
        // Filter non left-click events
        if (event.action != Action.LEFT_CLICK_BLOCK)
            return
        val block  = event.clickedBlock
        val player = event.player
        val item   = event.item ?: return
        // Make sure the correct block/item pair is used
        if (block?.type != Material.LODESTONE || !item.isWarpKey())
            return
        val name = WarpNameService[block.location] ?: localization["unnamed-waystone"]
        val state = block.getWarpState(player)
        // Skip any non-warp blocks
        if (state == WarpErrorState.None)
            return
        player.sendActionMessage(localization["waystone-info", name, state])
        event.cancel()
    }
}
