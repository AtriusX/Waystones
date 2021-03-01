package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_AIR
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import xyz.atrius.waystones.TeleportManager
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.handler.KeyHandler
import xyz.atrius.waystones.handler.WaystoneHandler
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.hasMovedBlock
import xyz.atrius.waystones.utility.sendActionError

class WarpEvent(private val names : WarpNameService) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        // Don't start warp while flying with elytra, not right-clicking, or a lodestone was clicked
        if (player.isGliding
            || event.action != RIGHT_CLICK_AIR
            || event.clickedBlock?.type == Material.LODESTONE
        ) return
        // Handle key actions and terminate if handler fails
        val key = KeyHandler(player, event)
        if (!key.handle())
            return player.sendActionError(key)
        // Make sure the key is connected before we continue
        val location = key.getLocation() ?: return
        val name = names[location] ?: "Waystone"
        // Handle key actions and terminate if handler fails
        val warp = WaystoneHandler(player, location, name)
        if (!warp.handle())
            return player.sendActionError(warp)
        // Queue the teleport then use key and warp on success
        TeleportManager.queueEvent(player, warp) {
            key.useKey()
            warp.teleport()
        }
        event.cancel()
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        if (!event.hasMovedBlock())
            return
        val player = event.player
        if (player !in TeleportManager)
            return
        TeleportManager.cancel(player)
        player.sendActionError("Teleportation cancelled")
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (!configuration.damageStopsWarping() || entity !is Player)
            return
        TeleportManager.cancel(entity)
        entity.sendActionError("Teleportation interrupted")
    }
}
