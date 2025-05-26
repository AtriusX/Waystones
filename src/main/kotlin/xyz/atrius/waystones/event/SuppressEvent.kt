package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.BLOCKED
import xyz.atrius.waystones.utility.awardAdvancement
import xyz.atrius.waystones.utility.isWaystone

@Single
class SuppressEvent : Listener {

    @EventHandler
    fun onSuppress(event: BlockPlaceEvent) {
        val block = event.block
        val above = block.world.getBlockAt(block.location.add(0.0, 1.0, 0.0))
        if (block.type == Material.OBSIDIAN && above.isWaystone())
            event.player.awardAdvancement(BLOCKED)
    }
}