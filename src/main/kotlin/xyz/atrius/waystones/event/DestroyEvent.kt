package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityExplodeEvent
import xyz.atrius.waystones.service.WarpNameService

object DestroyEvent : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block
        if (block.type == Material.LODESTONE)
            WarpNameService.remove(block.location)
    }

    @EventHandler(ignoreCancelled = true)
    fun onExplode(event: BlockExplodeEvent) = destroy(event.blockList())

    @EventHandler(ignoreCancelled = true)
    fun onEntityExplode(event: EntityExplodeEvent) = destroy(event.blockList())

    private fun destroy(blocks: List<Block>) =
        WarpNameService.remove(blocks.firstOrNull { it.type == Material.LODESTONE }?.location)
}
