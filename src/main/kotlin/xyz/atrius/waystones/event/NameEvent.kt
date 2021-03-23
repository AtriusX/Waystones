package xyz.atrius.waystones.event

import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.handler.HandleState.Success
import xyz.atrius.waystones.handler.NameHandler
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionMessage

object NameEvent : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != RIGHT_CLICK_BLOCK)
            return
        val player = event.player
        val item = player.inventory.itemInMainHand
        val block = event.clickedBlock
        val handler = NameHandler(player, item, block ?: return)
        when (handler.handle()) {
            Success -> {
                val name = handler.createName() ?: return
                player.sendActionMessage(localization["waystone-set-name", name])
                player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f, 2f)
                event.cancel()
            }
            else -> return
        }
    }
}