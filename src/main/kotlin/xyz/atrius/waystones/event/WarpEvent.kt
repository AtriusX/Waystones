package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.entity.Skeleton
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_AIR
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.advancement.SecretTunnelAdvancement
import xyz.atrius.waystones.advancement.ShootTheMessengerAdvancement
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.property.DamageStopsWarpingProperty
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.service.KeyService
import xyz.atrius.waystones.service.TeleportService
import xyz.atrius.waystones.service.WaystoneService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.hasMovedBlock
import xyz.atrius.waystones.utility.sendActionError
import xyz.atrius.waystones.utility.sendActionMessage

@Single
class WarpEvent(
    private val teleportService: TeleportService,
    private val localization: Localization,
    private val damageStopsWarping: DamageStopsWarpingProperty,
    private val keyService: KeyService,
    private val waystoneService: WaystoneService,
    private val advancementManager: AdvancementManager,
    private val secretTunnelAdvancement: SecretTunnelAdvancement,
    private val shootTheMessenger: ShootTheMessengerAdvancement,
) : Listener {

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        // Don't start warp while flying with elytra, not right-clicking, or a lodestone was clicked
        if (player.isGliding
            || (event.action != RIGHT_CLICK_AIR && event.action != RIGHT_CLICK_BLOCK)
            || event.clickedBlock?.type == Material.LODESTONE
        ) {
            return
        }
        // Make sure the key is connected before we continue
        val key = keyService
            .process(player, event)
            .fold (
                { return player.sendActionError(it.message()) },
                { it },
            )
        // Handle key actions and terminate if handler fails
        val warp = waystoneService
            .process(player, key.location.block, key.location)
            .fold(
                { return player.sendActionError(it.message()) },
                { it },
            )

        teleportService.queueEvent(warp) {
            key.useKey()
            warp.teleport()
            player.sendActionMessage(localization["warp-success"])
            advancementManager.awardAdvancement(player, secretTunnelAdvancement)
//            warp.gigawarpAdvancement()
//            warp.cleanEnergyAdvancement()
        }
        event.cancel()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onMove(event: PlayerMoveEvent) {
        if (!event.hasMovedBlock()) {
            return
        }
        val player = event.player

        if (player !in teleportService) {
            return
        }

        teleportService.cancel(player)
        player.sendActionError(localization["warp-cancel"])
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (!damageStopsWarping.value || entity !is Player) {
            return
        }
        // Don't cancel anything unless the entity is currently queued
        if (entity !in teleportService) {
            return
        }

        teleportService.cancel(entity)
        entity.sendActionError(localization["warp-interrupt"])
        waystoneAdvancement(entity)
    }

    private fun waystoneAdvancement(player: Player) {
        val attacker = (player.lastDamageCause as? EntityDamageByEntityEvent)?.damager

        if (attacker !is Arrow || attacker.shooter !is Skeleton) {
            return
        }

        if (player.health in 1.0..2.0) {
            advancementManager.awardAdvancement(player, shootTheMessenger)
        }
    }
}
