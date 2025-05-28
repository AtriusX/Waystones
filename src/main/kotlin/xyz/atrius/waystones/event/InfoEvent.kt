package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.WarpActiveState.Active
import xyz.atrius.waystones.data.WarpActiveState.Infinite
import xyz.atrius.waystones.data.WarpErrorState
import xyz.atrius.waystones.data.WarpState
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.service.KeyService
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.service.WaystoneService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.isSafe
import xyz.atrius.waystones.utility.sendActionMessage

@Single
class InfoEvent(
    private val localization: Localization,
    private val warpNameService: WarpNameService,
    private val keyService: KeyService,
    private val waystoneService: WaystoneService,
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onClick(event: PlayerInteractEvent) {
        // Filter non left-click events
        if (event.action != Action.LEFT_CLICK_BLOCK) {
            return
        }

        val block = event.clickedBlock
        val player = event.player
        val item = event.item
            ?: return
        // Make sure the correct block/item pair is used
        if (block?.type != Material.LODESTONE || !keyService.isWarpKey(item)) {
            return
        }

        val name = warpNameService[block.location]
            ?: localization["unnamed-waystone"]
        val state = block.getWarpState(player)
        // Skip any non-warp blocks
        if (state == WarpErrorState.None) {
            return
        }

        player.sendActionMessage(localization["waystone-info", name, state])
        event.cancel()
    }

    fun Block.getWarpState(player: Player): WarpState = when {
        !waystoneService.isWaystone(this) -> WarpErrorState.None
        waystoneService.isInhibited(this) -> WarpErrorState.Inhibited
        !waystoneService.hasPower(this, player) -> WarpErrorState.Unpowered
        !location.isSafe -> WarpErrorState.Obstructed
        waystoneService.hasInfinitePower(this) -> Infinite
        else -> Active(waystoneService.range(location))
    }
}
