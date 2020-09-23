package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*
import java.awt.Color

class LinkEvent(private val names: WarpNameService, private val config: Config) : Listener {

    @EventHandler
    fun onSet(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return
        val item   = event.item ?: return
        if (item.type != Material.COMPASS || event.clickedBlock?.type != Material.LODESTONE)
            return
        val player = event.player
        val block  = event.clickedBlock!!
        if (!config.relinkableKeys && (item.itemMeta as CompassMeta).hasLodestone()) {
            player.sendActionError("The destination for this key has been sealed")
            return event.cancel()
        }
        event.cancel()
        val key = ItemStack(Material.COMPASS)
        key.update<CompassMeta> {
            lodestone = block.location
            isLodestoneTracked = true
            lore = listOf(
                "${ChatColor.DARK_PURPLE}${names[block.location] ?: "Warpstone"}: [${block.location.locationCode}]"
            )
            setDisplayName("${ChatColor.of(Color.ORANGE)}Warpstone Key")
        }
        if (item.amount > 1) {
            player.inventory.addItem(key)
            if (!player.immortal)
                item.amount--
        } else {
            item.itemMeta = key.itemMeta
        }
        player.playSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)
    }
}