package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*

class InfoEvent(private val names: WarpNameService, private val config: Config) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK)
            return
        val block  = event.clickedBlock
        val player = event.player
        val item   = player.inventory.itemInMainHand
        if (block?.type == Material.LODESTONE && item.type == Material.COMPASS) {
            player.sendActionMessage(
                "Name: ${
                    names[block.location] ?: "None"
                } ${
                    when(config.limitDistance) {
                        block.hasInfinitePower() -> "| Range: Infinite"
                        block.isInhibited()      -> "| Status: Inhibited"
                        else                     -> "| Range: ${block.location.range(config)}"
                    }
                }", ChatColor.DARK_AQUA
            )
            event.cancel()
        }
    }
}