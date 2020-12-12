package xyz.atrius.waystones.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.handler.LinkHandler
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionError

class LinkEvent(private val names : WarpNameService) : Listener {

    @EventHandler
    fun onSet(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return
        val player = event.player
        val item = event.item ?: return
        val block = event.clickedBlock ?: return
        val linker = LinkHandler(player, item, block, names)
        if (!linker.handle())
            return player.sendActionError(linker)
        linker.link()
        event.cancel()
    }
}