package xyz.atrius.waystones.service

import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.advancement.IDontFeelSoGoodAdvancement
import xyz.atrius.waystones.animation.AnimationManager
import xyz.atrius.waystones.animation.effect.SimpleTeleportEffect
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.property.*
import xyz.atrius.waystones.data.config.property.type.SicknessOption
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.utility.*
import kotlin.random.Random

@Single
class TeleportService(
    private val animationManager: AnimationManager,
    private val localization: Localization,
    private val warpAnimations: WarpAnimationsProperty,
    private val waitTime: WaitTimeProperty,
    private val portalSickness: PortalSicknessProperty,
    private val portalSicknessWarping: PortalSicknessWarpingProperty,
    private val portalSicknessDamage: PortalSicknessDamageProperty,
    private val portalSicknessChance: PortalSicknessChanceProperty,
    private val advancementManager: AdvancementManager,
    private val iDontFeelSoGoodAdvancement: IDontFeelSoGoodAdvancement,
    private val powerCost: PowerCostProperty
) {
    private val queuedTeleports = HashMap<Player, Int>()

    fun queueEvent(warp: WaystoneService.Warp, key: KeyService.Key, onComplete: () -> Unit = {}) {
        val player = warp.player
        // Cancel any previous queues for this player
        if (contains(player)) {
            cancel(player)
        }
        // Queue the task and store the task id for if we need to cancel sooner
        queuedTeleports[player] = animationManager.register(
            SimpleTeleportEffect(warp, warpAnimations, waitTime, localization),
            warp.warpLocation
        ) {
            queuedTeleports.remove(player)
            key.useKey()
            teleport(player, warp)
            onComplete()
        }
    }

    private fun teleport(player: Player, warp: WaystoneService.Warp) {
        val location = player.location
        val warpLocation = warp.warpLocation
        val block = warpLocation.block
        // Teleport and notify the player
        player.teleport(warpLocation.UP.center.also {
            it.yaw = location.yaw
            it.pitch = location.pitch
        })
        // Skip de-buffs if the player is immortal or portal sickness is disabled
        if (player.immortal || !portalSickness.value) {
            player.sendActionMessage(localization["warp-success"])
            return
        }
        // Use power if the warp requires it
        if (warp.usePower) {
            block.powerBlock?.update<RespawnAnchor> {
                charges -= powerCost.value
            }
        }

        val sick = player.hasPortalSickness()
        // Damage the player if damage is enabled and they are already sick
        if (sick && portalSicknessWarping.value == SicknessOption.DAMAGE_ON_TELEPORT) {
            player.damage(portalSicknessDamage.value)
        }
        // If the player isn't sick, give them a chance to avoid getting sick
        if (!sick && Random.nextDouble() < portalSicknessChance.value) {
            player.sendActionMessage(localization["warp-success"])
            return
        }
        // Give portal sickness to the player
        player.addPotionEffects(
            PotionEffect(PotionEffectType.NAUSEA, 600, 9),
            PotionEffect(PotionEffectType.BLINDNESS, 100, 9)
        )
        advancementManager.awardAdvancement(player, iDontFeelSoGoodAdvancement)
        player.sendActionMessage(localization["warp-sickness"])
    }

    fun cancel(player: Player) {
        val id = queuedTeleports
            .remove(player)
            ?: -1

        animationManager.cancel(id)
    }

    operator fun contains(player: Player) =
        player in queuedTeleports
}