package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import xyz.atrius.waystones.data.advancement.UNLIMITED_POWER
import xyz.atrius.waystones.utility.awardAdvancement
import xyz.atrius.waystones.utility.isWaystone

object ExplodeEvent : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock
        if (block?.type != Material.RESPAWN_ANCHOR)
            return
        val anchor = (block.blockData as RespawnAnchor)
        val canExplode = block.world.environment == World.Environment.NORMAL
        val above = block.world.getBlockAt(block.location.add(0.0, 1.0, 0.0))
        if (anchor.charges == anchor.maximumCharges
            && above.isWaystone()
            && canExplode
        )
            player.awardAdvancement(UNLIMITED_POWER)
    }
}