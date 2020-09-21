package xyz.atrius.waystones.event

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.sendActionMessage
import java.awt.Color

class WaystoneNameEvent(private val names: WarpNameService) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != RIGHT_CLICK_BLOCK)
            return
        val player = event.player
        val inv    = player.inventory
        val item   = inv.itemInMainHand
        if (item.type != Material.NAME_TAG)
            return
        val block = event.clickedBlock
        if (block?.type != Material.LODESTONE)
            return
        val meta = item.itemMeta
        if (meta?.displayName == null)
            return

        names.add(block.location, meta.displayName)
        player.sendActionMessage("Waystone name set to ${meta.displayName}", Color.CYAN)
        if (player.gameMode != GameMode.CREATIVE)
            item.amount--
        event.isCancelled = true
    }
}