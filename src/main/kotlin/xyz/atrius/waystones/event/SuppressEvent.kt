package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.BlockedAdvancement
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.service.WaystoneService
import xyz.atrius.waystones.utility.UP

@Single
class SuppressEvent(
    private val advancementManager: AdvancementManager,
    private val waystoneService: WaystoneService,
    private val blockedAdvancement: BlockedAdvancement,
): Listener {

    @EventHandler
    fun onSuppress(event: BlockPlaceEvent) {
        val block = event.block
        val above = block.location.UP.block

        if (block.type == Material.OBSIDIAN && waystoneService.isWaystone(above)) {
            advancementManager.awardAdvancement(event.player, blockedAdvancement)
        }
    }
}