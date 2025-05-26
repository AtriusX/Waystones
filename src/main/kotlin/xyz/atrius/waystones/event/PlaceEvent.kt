package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.FloodFill
import xyz.atrius.waystones.data.advancement.HEAVY_ARTILLERY
import xyz.atrius.waystones.utility.awardAdvancement
import xyz.atrius.waystones.utility.isWaystone

@Single
class PlaceEvent : Listener {

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val block = event.block
        if (block.type != Material.NETHERITE_BLOCK)
            return
        val blocks = FloodFill(
            block.location,
            configuration.maxWarpSize(),
            *configuration.defaultBlocks,
            Material.LODESTONE
        )
        if (blocks.breakdown.keys.any(Block::isWaystone))
            event.player.awardAdvancement(HEAVY_ARTILLERY)
    }
}