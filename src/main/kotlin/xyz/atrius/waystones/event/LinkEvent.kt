package xyz.atrius.waystones.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.handler.HandleState.Fail
import xyz.atrius.waystones.handler.HandleState.Success
import xyz.atrius.waystones.handler.LinkHandler
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionError

object LinkEvent : Listener {

    @EventHandler
    fun onSet(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return
        val player = event.player
        val item = event.item ?: return
        val block = event.clickedBlock ?: return
        val linker = LinkHandler(player, item, block)
        when (val result = linker.handle()) {
            is Fail -> {
                player.sendActionError(result)
                event.cancel()
            }
            Success -> {
                linker.link()
                event.cancel()
            }
            else -> Unit
        }
    }
}