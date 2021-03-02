package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.handler.NameHandler
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionMessage

class NameEvent(private val names: WarpNameService) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != RIGHT_CLICK_BLOCK)
            return
        val player = event.player
        val item = player.inventory.itemInMainHand
        val block = event.clickedBlock
        val namer = NameHandler(player, item, block ?: return, names)
        if (!namer.handle())
            return
        val name = namer.createName() ?: return
        player.sendActionMessage("Waystone name set to $name", ChatColor.AQUA)
        player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f, 2f)
        event.cancel()
    }
}