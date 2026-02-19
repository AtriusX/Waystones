package xyz.atrius.waystones.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import org.bukkit.Location
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.data.config.property.MaxXpUsageProperty
import xyz.atrius.waystones.data.config.property.UseXpProperty
import xyz.atrius.waystones.data.config.property.type.Power
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.manager.LocalizedString

@Single
class XpValidationService(
    private val useXpProperty: UseXpProperty,
    private val maxXpUsageProperty: MaxXpUsageProperty,
    private val localization: LocalizationManager,
) {

    fun process(
        player: Player,
        waystoneLocation: Location,
        maxDistance: Double,
    ): Either<XpValidationError, Unit> = either {
        val useXp = useXpProperty.value()
        val location = player.location
        val sameWorld = location.world.uid == waystoneLocation.world.uid
        // We don't need to charge any XP if the config is set to NONE, or if the
        // power type is INTER_DIMENSION and the player is in the same world.
        when (useXp) {
            Power.NONE -> return@either
            Power.INTER_DIMENSION if sameWorld -> return@either
            else -> {}
        }
        // Avoid using the regular distance method, as it uses the square root function
        val maxDistSq = maxDistance * maxDistance
        val distanceSq = waystoneLocation.distanceSquared(location)
        // Calculate the amount of XP to deduct based on distance from the waystone
        val xpRatio = distanceSq / maxDistSq
        val xp = maxXpUsageProperty.value()
        val requiredXp = (xpRatio * xp.amount).toInt()
        // Determine whether to treat the XP cost as levels or raw XP
        if (xp.asLevels) {

            ensure(player.level - requiredXp >= 0) {
                XpValidationError.InsufficientLevels(localization, requiredXp, player.level)
            }

            player.level -= requiredXp
        } else {

            ensure(player.totalExperience - requiredXp >= 0) {
                XpValidationError.InsufficientXp(localization, requiredXp, player.totalExperience)
            }

            player.totalExperience -= requiredXp
        }
    }

    sealed class XpValidationError(val message: () -> LocalizedString) {

        class InsufficientXp(localization: LocalizationManager, required: Int, current: Int) : XpValidationError(
            { localization["insufficient-xp", required, current] })

        class InsufficientLevels(localization: LocalizationManager, required: Int, current: Int) : XpValidationError(
            { localization["insufficient-levels", required, current] })
    }

    companion object {

        private val logger = LoggerFactory.getLogger(XpValidationService::class.java.name)
    }
}