package xyz.atrius.waystones.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.WAYSTONES
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.handler.HandleState.Fail
import xyz.atrius.waystones.handler.HandleState.Success
import xyz.atrius.waystones.handler.LinkHandler
import xyz.atrius.waystones.utility.awardAdvancement
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionError

@Single
class LinkEvent(
    private val localization: Localization,
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onSet(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return
        val player = event.player
        val item = event.item ?: return
        val block = event.clickedBlock ?: return
        val linker = LinkHandler(player, item, block, localization)
        when (val result = linker.handle()) {
            is Fail -> {
                player.sendActionError(result)
                event.cancel()
            }
            Success -> {
                linker.link()
                event.cancel()
                player.awardAdvancement(WAYSTONES)
            }
            else -> Unit
        }
    }
}
