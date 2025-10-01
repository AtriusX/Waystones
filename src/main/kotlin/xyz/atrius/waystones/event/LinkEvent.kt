package xyz.atrius.waystones.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.advancement.WaystonesAdvancement
import xyz.atrius.waystones.dao.WaystoneInfo
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.service.LinkService
import xyz.atrius.waystones.service.LinkService.LinkServiceError
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionError

@Single
class LinkEvent(
    private val linkService: LinkService,
    private val advancementManager: AdvancementManager,
    private val waystonesAdvancement: WaystonesAdvancement,
    private val waystoneInfoRepository: WaystoneInfoRepository,
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onSet(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val player = event.player
        val item = event.item ?: return
        val block = event.clickedBlock ?: return

        linkService
            .process(player, item, block)
            .onLeft {
                if (it is LinkServiceError.Ignore) {
                    return
                }

                player.sendActionError(it.message())
                event.cancel()
            }
        // Check if an entry exists already before saving the waystone again
        waystoneInfoRepository
            .existsByLocation(block.location)
            .thenApplyAsync { exists ->
                // If the location isn't present in the database, it should be safe to write
                if (!exists) {
                    waystoneInfoRepository.save(WaystoneInfo.fromLocation(block.location))
                }
            }
        advancementManager.awardAdvancement(player, waystonesAdvancement)
        event.cancel()
        // This is silly but due to https://github.com/PaperMC/Paper/issues/12954 the inventory is getting
        // de-synced from the server. To ensure our linked keys do not get duplicated, we force an update
        // event here to keep the server and client synchronized. Once the bug is fixed, this can be removed.
        player.updateInventory()
    }
}
