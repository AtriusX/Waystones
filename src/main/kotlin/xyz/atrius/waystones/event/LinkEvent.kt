package xyz.atrius.waystones.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.advancement.WaystonesAdvancement
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.service.LinkService
import xyz.atrius.waystones.service.LinkService.LinkServiceError
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionError

@Single
class LinkEvent(
    private val linkService: LinkService,
    private val advancementManager: AdvancementManager,
    private val waystonesAdvancement: WaystonesAdvancement,
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

        advancementManager.awardAdvancement(player, waystonesAdvancement)
        event.cancel()
    }
}
