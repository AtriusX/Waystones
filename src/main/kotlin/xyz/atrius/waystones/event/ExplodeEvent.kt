package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.UnlimitedPowerAdvancement
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.service.WaystoneService

@Single
class ExplodeEvent(
    private val advancementManager: AdvancementManager,
    private val waystoneService: WaystoneService,
    private val unlimitedPowerAdvancement: UnlimitedPowerAdvancement,
) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock

        if (block?.type != Material.RESPAWN_ANCHOR || event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val anchor = (block.blockData as RespawnAnchor)
        val canExplode = block.world.environment == World.Environment.NORMAL
        val above = block.world.getBlockAt(block.location.add(0.0, 1.0, 0.0))

        if (anchor.charges == anchor.maximumCharges
            && waystoneService.isWaystone(above)
            && canExplode
        ) {
            advancementManager.awardAdvancement(player, unlimitedPowerAdvancement)
        }
    }
}