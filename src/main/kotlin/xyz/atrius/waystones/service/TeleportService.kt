package xyz.atrius.waystones.service

import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.advancement.IDontFeelSoGoodAdvancement
import xyz.atrius.waystones.animation.AnimationManager
import xyz.atrius.waystones.animation.effect.SimpleTeleportEffect
import xyz.atrius.waystones.data.config.property.EnablePortalSicknessProperty
import xyz.atrius.waystones.data.config.property.PortalSicknessChanceProperty
import xyz.atrius.waystones.data.config.property.PortalSicknessDamageProperty
import xyz.atrius.waystones.data.config.property.PortalSicknessWarpingProperty
import xyz.atrius.waystones.data.config.property.PowerCostProperty
import xyz.atrius.waystones.data.config.property.WaitTimeProperty
import xyz.atrius.waystones.data.config.property.WarpAnimationsProperty
import xyz.atrius.waystones.data.config.property.type.SicknessOption
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.utility.UP
import xyz.atrius.waystones.utility.addPotionEffects
import xyz.atrius.waystones.utility.center
import xyz.atrius.waystones.utility.hasPortalSickness
import xyz.atrius.waystones.utility.immortal
import xyz.atrius.waystones.utility.powerBlock
import xyz.atrius.waystones.utility.sendActionMessage
import xyz.atrius.waystones.utility.update
import kotlin.random.Random

@Single
class TeleportService(
    private val animationManager: AnimationManager,
    private val localization: LocalizationManager,
    private val warpAnimations: WarpAnimationsProperty,
    private val waitTime: WaitTimeProperty,
    private val portalSickness: EnablePortalSicknessProperty,
    private val portalSicknessWarping: PortalSicknessWarpingProperty,
    private val portalSicknessDamage: PortalSicknessDamageProperty,
    private val portalSicknessChance: PortalSicknessChanceProperty,
    private val advancementManager: AdvancementManager,
    private val iDontFeelSoGoodAdvancement: IDontFeelSoGoodAdvancement,
    private val powerCost: PowerCostProperty,
) {
    private val queuedTeleports = HashMap<Player, Int>()

    fun queueEvent(warp: WaystoneService.Warp, key: KeyService.Key, onComplete: () -> Unit = {}) {
        val player = warp.player
        // Prevent spam queueing
        if (player in queuedTeleports) {
            return
        }
        // Queue the task and store the task id for if we need to cancel sooner
        queuedTeleports[player] = animationManager.register(
            SimpleTeleportEffect(warp, warpAnimations, waitTime, localization),
            warp.player,
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
        player.teleport(
            warpLocation.UP.center.also {
                it.yaw = location.yaw
                it.pitch = location.pitch
            }
        )
        // Skip de-buffs if the player is immortal or portal sickness is disabled
        if (player.immortal || !portalSickness.value()) {
            player.sendActionMessage(localization["warp-success"])
            return
        }
        // Use power if the warp requires it
        if (warp.usePower) {
            block.powerBlock?.update<RespawnAnchor> {
                charges -= powerCost.value()
            }
        }

        val sick = player.hasPortalSickness()
        // Damage the player if damage is enabled and they are already sick
        if (sick && portalSicknessWarping.value() == SicknessOption.DAMAGE_ON_TELEPORT) {
            player.damage(portalSicknessDamage.value())
        }
        // If the player isn't sick, give them a chance to avoid getting sick
        if (!sick && Random.nextDouble() > portalSicknessChance.value()) {
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

        animationManager.cancel(id, player)
    }

    operator fun contains(player: Player) =
        player in queuedTeleports
}
