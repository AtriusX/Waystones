package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.repository.WaystoneInfoRepository

@Single
class DestroyEvent(
    private val waystoneInfoRepository: WaystoneInfoRepository,
) : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block

        if (block.type != Material.LODESTONE) {
            return
        }

        waystoneInfoRepository.deleteByLocation(block.location)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onExplode(event: BlockExplodeEvent) = destroy(event.blockList())

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityExplode(event: EntityExplodeEvent) = destroy(event.blockList())

    private fun destroy(blocks: List<Block>) = blocks
        .filter { it.type == Material.LODESTONE }
        .map { it.location }
        .onEach { waystoneInfoRepository.deleteByLocation(it) }
}
