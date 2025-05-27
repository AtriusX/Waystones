package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.BLOCKED
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.utility.UP
import xyz.atrius.waystones.utility.isWaystone

@Single
class SuppressEvent(
    private val advancementManager: AdvancementManager,
): Listener {

    @EventHandler
    fun onSuppress(event: BlockPlaceEvent) {
        val block = event.block
        val above = block.location.UP.block

        if (block.type == Material.OBSIDIAN && above.isWaystone()) {
            advancementManager.awardAdvancement(event.player, BLOCKED)
        }
    }
}