package xyz.atrius.waystones.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Beacon
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.koin.core.annotation.Single
import xyz.atrius.waystones.advancement.CleanEnergyAdvancement
import xyz.atrius.waystones.advancement.GigawarpsAdvancement
import xyz.atrius.waystones.data.FloodFill
import xyz.atrius.waystones.data.config.property.BaseDistanceProperty
import xyz.atrius.waystones.data.config.property.JumpWorldsProperty
import xyz.atrius.waystones.data.config.property.LimitDistanceProperty
import xyz.atrius.waystones.data.config.property.MaxWarpSizeProperty
import xyz.atrius.waystones.data.config.property.PowerCostProperty
import xyz.atrius.waystones.data.config.property.RequirePowerProperty
import xyz.atrius.waystones.data.config.property.type.Power
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.manager.LocalizedString
import xyz.atrius.waystones.utility.isActive
import xyz.atrius.waystones.utility.isSafe
import xyz.atrius.waystones.utility.powerBlock
import xyz.atrius.waystones.utility.sameDimension

@Single
class WaystoneService(
    private val localization: LocalizationManager,
    private val jumpWorlds: JumpWorldsProperty,
    private val maxWarpSize: MaxWarpSizeProperty,
    private val baseDistance: BaseDistanceProperty,
    private val requirePower: RequirePowerProperty,
    private val powerCost: PowerCostProperty,
    private val boostBlockService: BoostBlockService,
    private val warpNameService: WarpNameService,
    private val advancementManager: AdvancementManager,
    private val cleanEnergyAdvancement: CleanEnergyAdvancement,
    private val gigawarpsAdvancement: GigawarpsAdvancement,
    private val worldRatioService: WorldRatioService,
    private val limitDistance: LimitDistanceProperty,
) {

    fun process(player: Player, block: Block, keyLocation: Location): Either<WaystoneServiceError, Warp> = either {
        // Ensure the warp is valid to use
        val name = warpNameService[keyLocation]
            ?.format(player)
            ?: localization["unnamed-waystone"]
                .format(player)
        val distance = validateWarp(player, block, name).bind()
        // Determine how the waystone requires power
        val usePower = when (requirePower.value()) {
            Power.ALL -> true
            Power.INTER_DIMENSION -> !hasInfinitePower(block) &&
                !player.location.sameDimension(block.world)

            Power.NONE -> false
        }

        Warp(name, player, block.location, distance, usePower)
    }

    fun range(waystone: Location): Int {
        val range = FloodFill(waystone, maxWarpSize.value(), *boostBlockService.defaultBlocks)
            .breakdown
            .entries
            .sumOf { (block, count) -> count * (boostBlockService.blockMappings[block.type]?.invoke() ?: 1) }

        return baseDistance.value() + range
    }

    private fun validateWarp(
        player: Player,
        block: Block,
        name: String,
    ): Either<WaystoneServiceError, Double> = either {
        ensure(isWaystone(block)) {
            WaystoneServiceError.WaystoneSevered(localization, name)
        }

        val state = when (getWarpState(player, block)) {
            null -> WaystoneServiceError.WaystoneSevered(localization, name)
            is WaystoneStatus.Inhibited -> WaystoneServiceError.WaystoneInhibited(localization, name)
            is WaystoneStatus.Unpowered -> WaystoneServiceError.WaystoneUnpowered(localization, name)
            is WaystoneStatus.Obstructed -> WaystoneServiceError.WaystoneObstructed(localization, name)
            else -> null
        }

        if (state != null) {
            raise(state)
        }

        val interDimension = player.location.sameDimension(block.location)

        if (interDimension) {
            ensure(jumpWorlds.value()) {
                WaystoneServiceError.WaystoneWorldJumpDisabled(localization)
            }
        }

        val ratio = worldRatioService.getRatio(player.world, block.world)
            ?: raise(WaystoneServiceError.WaystoneWorldJumpDisabled(localization))

        val range = range(block.location) / ratio
        val distance = calculateDistance(player.location, block.location, 1.0)
        val name = warpNameService[block.location]
            ?.format(player)
            ?: localization["unnamed-waystone"]
                .format(player)

        if (!hasInfinitePower(block) && limitDistance.value()) {
            ensure(distance <= range) {
                WaystoneServiceError.WaystoneOutOfRange(localization, name, distance, range, block.location)
            }
        }

        distance
    }

    fun gigawarpsAdvancement(player: Player, warp: Warp) {
        if (advancementManager.hasAdvancement(player, gigawarpsAdvancement)) {
            return
        }

        if (warp.distance > boostBlockService.maxDistance() / 2) {
            advancementManager.awardAdvancement(player, gigawarpsAdvancement)
        }
    }

    fun cleanEnergyAdvancement(player: Player, warp: Warp) {
        if (advancementManager.hasAdvancement(player, cleanEnergyAdvancement)) {
            return
        }

        val fill = FloodFill(
            warp.warpLocation,
            maxWarpSize.value(),
            *boostBlockService.defaultBlocks,
            Material.BEACON
        )
        val activeBeaconPresent = fill.breakdown
            .any { (block) -> (block.state as? Beacon)?.isActive() == true }

        if (activeBeaconPresent) {
            advancementManager.awardAdvancement(player, cleanEnergyAdvancement)
        }
    }

    private fun calculateDistance(from: Location, to: Location, ratio: Double): Double {
        val toNormalized = to
            .toVector()
            .multiply(Vector(ratio, 1.0, ratio))

        return from.toVector().distance(toNormalized)
    }

    data class Warp(
        val name: String,
        val player: Player,
        val warpLocation: Location,
        val distance: Double,
        val usePower: Boolean,
    )

    fun isWaystone(block: Block?): Boolean =
        block?.type == Material.LODESTONE

    private val Block.isPowered: Boolean
        get() = !isInhibited(this) && (hasInfinitePower(this) || hasNormalPower(this))

    private fun hasNormalPower(block: Block): Boolean {
        val respawnAnchor = block.powerBlock
            ?.blockData as? RespawnAnchor
        val charges = respawnAnchor
            ?.charges
            ?: 0

        return charges >= powerCost.value()
    }

    private fun hasPower(block: Block, player: Player): Boolean = when (requirePower.value()) {
        Power.INTER_DIMENSION -> block.location.sameDimension(player.location) || block.isPowered
        Power.ALL -> block.isPowered
        else -> true
    }

    private fun isInhibited(block: Block): Boolean =
        block.powerBlock?.type == Material.OBSIDIAN

    private fun hasInfinitePower(block: Block): Boolean =
        block.powerBlock?.type == Material.COMMAND_BLOCK

    fun getWarpState(player: Player, block: Block): WaystoneStatus? {
        if (!isWaystone(block)) {
            return null
        }

        if (isInhibited(block)) {
            return WaystoneStatus.Inhibited(localization)
        }

        if (!hasPower(block, player)) {
            return WaystoneStatus.Unpowered(localization)
        }

        if (!block.location.isSafe) {
            return WaystoneStatus.Obstructed(localization)
        }

        if (hasInfinitePower(block) || !limitDistance.value()) {
            return WaystoneStatus.Infinite(localization)
        }

        return WaystoneStatus.Active(localization, range(block.location))
    }

    sealed class WaystoneServiceError(val message: () -> LocalizedString?) {

        class WaystoneSevered(localization: LocalizationManager, name: String) :
            WaystoneServiceError({ localization["warp-error", name] })

        class WaystoneInhibited(localization: LocalizationManager, name: String) :
            WaystoneServiceError({ localization["warp-error-inhibited", name] })

        class WaystoneUnpowered(localization: LocalizationManager, name: String) :
            WaystoneServiceError({ localization["warp-error-unpowered", name] })

        class WaystoneObstructed(localization: LocalizationManager, name: String) :
            WaystoneServiceError({ localization["warp-error-obstructed", name] })

        class WaystoneWorldJumpDisabled(localization: LocalizationManager) :
            WaystoneServiceError({ localization["world-jump-disabled"] })

        class WaystoneOutOfRange(
            localization: LocalizationManager,
            name: String?,
            distance: Double,
            range: Double,
            location: Location
        ) : WaystoneServiceError(
            { localization["warp-out-of-range", name, distance - range, range, location.x, location.z] }
        )
    }

    sealed class WaystoneStatus(val message: () -> LocalizedString?) {

        class Inhibited(localization: LocalizationManager) :
            WaystoneStatus({ localization["waystone-status", -3] })

        class Unpowered(localization: LocalizationManager) :
            WaystoneStatus({ localization["waystone-status", -2] })

        class Obstructed(localization: LocalizationManager) :
            WaystoneStatus({ localization["waystone-status", -1] })

        class Active(localization: LocalizationManager, range: Int) :
            WaystoneStatus({ localization["waystone-status", 0, range] })

        class Infinite(localization: LocalizationManager) :
            WaystoneStatus({ localization["waystone-status", 1] })
    }
}
