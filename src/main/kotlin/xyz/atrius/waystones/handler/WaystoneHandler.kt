package xyz.atrius.waystones.handler

import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.atrius.waystones.Power
import xyz.atrius.waystones.SicknessOption
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.utility.*
import xyz.atrius.waystones.utility.WarpState.Active
import kotlin.math.round
import kotlin.random.Random

class WaystoneHandler(
        override val player: Player,
        val warpLocation: Location,
        val name: String
) : PlayerHandler {
    override var error: String? = null
        private set

    val location       = player.location
    val interDimension = !warpLocation.sameDimension(location)
    val block          = warpLocation.block
    val state          = block.getWarpState(player)

    override fun handle(): Boolean {
        error = state.message(name)
        if (state is Active) {
            // Calculate range and distance from warp
            val range    = state.range / if (interDimension) configuration.worldRatio else 1
            val distance = location.toVector().distance(warpLocation.toVector())
            return if (distance > range) false.also {
                error = distanceError(name, distance, range)
            } else true
        }
        return error == null
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
        if (sick && configuration.portalSickWarping == SicknessOption.DAMAGE_ON_TELEPORT)
            player.damage(configuration.portalSicknessDamage)
        // Give portal sickness to the player if they aren't immortal, are unlucky, or already are sick
        if (configuration.portalSickness
            && (sick || Random.nextDouble() < configuration.portalSicknessChance)
            && !block.hasInfinitePower()
        ) {
            player.addPotionEffects(
                    PotionEffect(PotionEffectType.CONFUSION, 600, 9),
                    PotionEffect(PotionEffectType.BLINDNESS, 100, 9)
            )
            player.sendActionMessage("You feel a chill in your bones...", ChatColor.DARK_GRAY)
        } else {
            player.sendActionMessage("Warped to $name", ChatColor.DARK_GREEN)
        }
        // Determine how power is depleted from the warp
        val power = configuration.requirePower
        if (power == Power.ALL || (interDimension && power == Power.INTER_DIMENSION)) {
            // Only deplete power if the power is not infinite
            if (!block.hasInfinitePower()) block.powerBlock?.update<RespawnAnchor> {
                charges--
            }
        }
    }

    private fun distanceError(name: String, distance: Double, range: Int): String =
            "$name is out of warp range [${round(distance - range).toInt()} block(s)]"
}