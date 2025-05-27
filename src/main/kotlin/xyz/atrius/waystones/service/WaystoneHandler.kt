package xyz.atrius.waystones.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import org.koin.core.annotation.Single
import xyz.atrius.waystones.Power
import xyz.atrius.waystones.SicknessOption
import xyz.atrius.waystones.data.FloodFill
import xyz.atrius.waystones.data.advancement.I_DONT_FEEL_SO_GOOD
import xyz.atrius.waystones.data.config.BoostBlockService
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.LocalizedString
import xyz.atrius.waystones.data.config.property.*
import xyz.atrius.waystones.utility.*
import kotlin.random.Random

@Single
class WaystoneService(
    private val localization: Localization,
    private val jumpWorlds: JumpWorldsProperty,
    private val maxWarpSize: MaxWarpSizeProperty,
    private val baseDistance: BaseDistanceProperty,
    private val portalSickness: PortalSicknessProperty,
    private val portalSicknessChance: PortalSicknessChanceProperty,
    private val portalSicknessDamage: PortalSicknessDamageProperty,
    private val portalSicknessWarping: PortalSicknessWarpingProperty,
    private val worldRatio: WorldRatioProperty,
    private val requirePower: RequirePowerProperty,
    private val powerCost: PowerCostProperty,
    private val boostBlockService: BoostBlockService,
    private val warpNameService: WarpNameService,
) {

    fun process(player: Player, block: Block, keyLocation: Location): Either<WaystoneServiceError,  Warp> = either {
        // Ensure the warp is valid to use
        validateWarp(player, block).bind()

        val name = warpNameService[keyLocation]
            ?: localization["unnamed-waystone"]
                .toString()

        Warp(name, player, block.location)
    }

    private fun Location.range(): Int {
        val range = FloodFill(this, maxWarpSize.value, *boostBlockService.defaultBlocks)
            .breakdown
            .entries
            .sumOf { (block, count) -> count * (boostBlockService.blockMappings[block.type]?.invoke() ?: 1) }

        return baseDistance.value + range
    }

    private fun validateWarp(player: Player, block: Block): Either<WaystoneServiceError, Unit> = either {

        ensure(block.isWaystone()) {
            WaystoneServiceError.WaystoneSevered(localization)
        }

        ensure(!block.isInhibited()) {
            WaystoneServiceError.WaystoneInhibited(localization)
        }

        ensure(block.hasPower(player)) {
            WaystoneServiceError.WaystoneUnpowered(localization)
        }

        ensure(block.location.isSafe) {
            WaystoneServiceError.WaystoneObstructed(localization)
        }

        val interDimension = player.location.sameDimension(block.location)

        if (interDimension) {
            ensure(jumpWorlds.value) {
                WaystoneServiceError.WaystoneWorldJumpDisabled(localization)
            }
        }

        if (!block.hasInfinitePower()) {
            val ratio = when (interDimension) {
                true -> 1.0
                else -> worldRatio.value
            }
            val range = block.location.range() / ratio
            val distance = calculateDistance(player.location, block.location, ratio)

            val name = warpNameService[block.location]
                ?: localization["unnamed-waystone"].format()

            ensure(distance <= range) {
                WaystoneServiceError.WaystoneOutOfRange(localization, name, distance, range)
            }
        }
    }

//    fun gigawarpAdvancement(player: Player,) {
//        if (distance > configuration.maxDistance() / 2)
//            player.awardAdvancement(GIGAWARPS)
//    }
//
//    fun cleanEnergyAdvancement() {
//        val fill = FloodFill(
//            warpLocation,
//            maxWarpSize.value,
//            *boostBlockService.defaultBlocks,
//            Material.BEACON
//        )
//        if (fill.breakdown.any { (block) ->
//                with (block.state) { this is Beacon && isActive() }
//            }) player.awardAdvancement(CLEAN_ENERGY)
//    }

    private fun calculateDistance(from: Location, to: Location, ratio: Double): Double {
        val toNormalized = to
            .toVector()
            .multiply(Vector(ratio, 1.0, ratio))

        return from.toVector().distance(toNormalized)
    }

    inner class Warp(
        val name: String,
        val player: Player,
        val warpLocation: Location,
    ) {
        private val location: Location = player.location
        private val block: Block = warpLocation.block
        private val interDimension: Boolean = !warpLocation.sameDimension(location)

        fun teleport() {
            // Teleport and notify the player
            player.teleport(warpLocation.UP.center.also {
                it.yaw   = location.yaw
                it.pitch = location.pitch
            })
            // Skip de-buffs if the player is immortal
            if (player.immortal) {
                return
            }
            val sick = player.hasPortalSickness()
            // Damage the player if damage is enabled and they aren't immortal
            if (sick && portalSicknessWarping.value == SicknessOption.DAMAGE_ON_TELEPORT) {
                player.damage(portalSicknessDamage.value)
            }
            // Give portal sickness to the player if they aren't immortal, are unlucky, or already are sick
            if (portalSickness.value
                && (sick || Random.nextDouble() < portalSicknessChance.value)
                && !block.hasInfinitePower()
            ) {
                player.addPotionEffects(
                    PotionEffect(PotionEffectType.NAUSEA, 600, 9),
                    PotionEffect(PotionEffectType.BLINDNESS, 100, 9)
                )
                player.sendActionMessage(localization["warp-sickness"])
                player.awardAdvancement(I_DONT_FEEL_SO_GOOD)
            } else {
                player.sendActionMessage(localization["warp-safely"])
            }
            // Determine how power is depleted from the warp
            val power = requirePower.value

            if (power == Power.ALL || (interDimension && power == Power.INTER_DIMENSION)) {
                // Only deplete power if the power is not infinite
                if (!block.hasInfinitePower()) block.powerBlock?.update<RespawnAnchor> {
                    charges -= powerCost.value.coerceIn(0, 4)
                }
            }
        }
    }

    sealed class WaystoneServiceError(val message: () -> LocalizedString?) {

        class WaystoneSevered(localization: Localization) :
            WaystoneServiceError({ localization["warp-error"] })

        class WaystoneInhibited(localization: Localization) :
            WaystoneServiceError({ localization["waystone-status-inhibited"] })

        class WaystoneUnpowered(localization: Localization) :
            WaystoneServiceError({ localization["waystone-status-unpowered"] })

        class WaystoneObstructed(localization: Localization) :
            WaystoneServiceError({ localization["waystone-status-obstructed"] })

        class WaystoneWorldJumpDisabled(localization: Localization) :
            WaystoneServiceError({ localization["waystone-world-jump-disabled"] })

        class WaystoneOutOfRange(localization: Localization, name: String?, distance: Double, range: Double) :
            WaystoneServiceError({ localization["warp-out-of-range", name, distance - range] })
    }
}
