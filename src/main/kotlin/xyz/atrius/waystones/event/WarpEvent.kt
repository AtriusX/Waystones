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
import xyz.atrius.waystones.handler.HandleState.Fail
import xyz.atrius.waystones.handler.HandleState.Success
import xyz.atrius.waystones.handler.KeyHandler
import xyz.atrius.waystones.handler.WaystoneHandler
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.hasMovedBlock
import xyz.atrius.waystones.utility.sendActionError
import xyz.atrius.waystones.utility.sendActionMessage

object WarpEvent : Listener {

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
        when (val result = key.handle()) {
            is Fail -> return player.sendActionError(result)
        }
        // Make sure the key is connected before we continue
        val location = key.getLocation() ?: return
        val name = WarpNameService[location] ?: localization["unnamed-waystone"].toString()
        // Handle key actions and terminate if handler fails
        val warp = WaystoneHandler(player, location, name)
        when (val result = warp.handle()) {
            is Fail -> return player.sendActionError(result)
            is Success -> {
                // Queue the teleport then use key and warp on success
                TeleportManager.queueEvent(player, warp) {
                    key.useKey()
                    warp.teleport()
                    player.sendActionMessage(localization["warp-success"])
                }
                event.cancel()
            }
        }
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        if (!event.hasMovedBlock())
            return
        val player = event.player
        if (player !in TeleportManager)
            return
        TeleportManager.cancel(player)
        player.sendActionError(localization["warp-cancel"])
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (!configuration.damageStopsWarping() || entity !is Player)
            return
        // Don't cancel anything unless the entity is currently queued
        if (entity !in TeleportManager)
            return
        TeleportManager.cancel(entity)
        entity.sendActionError(localization["warp-interrupt"])
    }
}
