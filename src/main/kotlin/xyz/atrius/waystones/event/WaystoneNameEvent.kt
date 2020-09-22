package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionMessage

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
        if (meta?.hasDisplayName() != true)
            return
        names.add(block.location, meta.displayName)
        player.sendActionMessage("Waystone name set to ${meta.displayName}", ChatColor.AQUA)
        player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f, 2f)
        if (player.gameMode != GameMode.CREATIVE)
            item.amount--
        event.cancel()
    }
}