package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*
import xyz.atrius.waystones.utility.WarpState.*

class InfoEvent(private val names : WarpNameService) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK)
            return
        val block  = event.clickedBlock
        val player = event.player
        val item   = player.inventory.itemInMainHand
        if (block?.type == Material.LODESTONE && item.isWarpKey()) {
            player.sendActionMessage(
                "Name: ${
                    names[block.location] ?: "None"
                } ${
                    when(block.getWarpState(player)) {
                        Infinite -> "| Range: Infinite"
                        Inhibited  -> "| Status: Inhibited"
                        Unpowered  -> "| Status: Unpowered"
                        Obstructed -> "| Status: Obstructed"
                        else       -> "| Range: ${block.location.range()}"
                    }
                }", ChatColor.DARK_AQUA
            )
            event.cancel()
        }
    }
}