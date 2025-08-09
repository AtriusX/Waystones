package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.service.KeyService
import xyz.atrius.waystones.service.WaystoneService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionMessage

@Single
class InfoEvent(
    private val localization: LocalizationManager,
    private val keyService: KeyService,
    private val waystoneService: WaystoneService,
    private val waystoneInfoRepository: WaystoneInfoRepository,
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

        val name = waystoneInfoRepository
            .getWaystone(block.location)
            .thenApplyAsync { it?.name ?: localization["unnamed-waystone"].format(player) }
        // Skip any non-warp blocks
        val state = waystoneService
            .getWarpState(player, block)
            ?.message()
            ?.format(player)
            ?: return

        player.sendActionMessage(localization["waystone-info", name.get(), state])
        event.cancel()
    }
}
