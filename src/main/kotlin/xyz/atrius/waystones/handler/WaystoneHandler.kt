package xyz.atrius.waystones.handler

import org.bukkit.Location
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import xyz.atrius.waystones.Power
import xyz.atrius.waystones.SicknessOption
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.WarpActiveState
import xyz.atrius.waystones.data.WarpErrorState
import xyz.atrius.waystones.handler.HandleState.Fail
import xyz.atrius.waystones.handler.HandleState.Success
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.*
import kotlin.random.Random

class WaystoneHandler(
    override val player: Player,
    val warpLocation: Location,
    val name: String
) : PlayerHandler {
    val location       = player.location
    val interDimension = !warpLocation.sameDimension(location)
    val block          = warpLocation.block
    val state          = block.getWarpState(player)

    override fun handle(): HandleState {
        return when (state) {
            is WarpActiveState -> {
                // Calculate range, sync ratio and distance from warp
                val range    = state.range / if (interDimension) configuration.worldRatio() else 1
                val type     = location.synchronize(warpLocation)
                val distance = location.toVector()
                    .distance(warpLocation.toVector().multiply(Vector(type.getRatio(), 1.0, type.getRatio())))
                if (distance > range)
                    Fail(distanceError(name, distance, range)) else Success
            }
            is WarpErrorState -> Fail(state.message(name))
        }
    }

    fun teleport() {
        // Teleport and notify the player
        player.teleport(warpLocation.UP.center.also {
            it.yaw   = location.yaw
            it.pitch = location.pitch
        })
        // Skip debuffs if the player is immortal
        if (player.immortal)
            return
        val sick = player.hasPortalSickness()
        // Damage the player if damage is enabled and they aren't immortal
        if (sick && configuration.portalSickWarping() == SicknessOption.DAMAGE_ON_TELEPORT)
            player.damage(configuration.portalSicknessDamage())
        // Give portal sickness to the player if they aren't immortal, are unlucky, or already are sick
        if (configuration.portalSickness()
            && (sick || Random.nextDouble() < configuration.portalSicknessChance())
            && !block.hasInfinitePower()
        ) {
            player.addPotionEffects(
                PotionEffect(PotionEffectType.CONFUSION, 600, 9),
                PotionEffect(PotionEffectType.BLINDNESS, 100, 9)
            )
            player.sendActionMessage(localization.localize("warp-sickness"))
        } else {
            player.sendActionMessage(localization.localize("warp-safely"))
        }
        // Determine how power is depleted from the warp
        val power = configuration.requirePower()
        if (power == Power.ALL || (interDimension && power == Power.INTER_DIMENSION)) {
            // Only deplete power if the power is not infinite
            if (!block.hasInfinitePower()) block.powerBlock?.update<RespawnAnchor> {
                charges -= configuration.powerCost()
            }
        }
    }

    private fun distanceError(name: String, distance: Double, range: Int): String =
        "$name is out of warp range [%.1f block(s)]".format(distance - range)
}
