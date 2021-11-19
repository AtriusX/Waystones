package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.block.Beacon
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import xyz.atrius.waystones.Power.ALL
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.WarpActiveState.Active
import xyz.atrius.waystones.data.WarpActiveState.Infinite
import xyz.atrius.waystones.data.WarpErrorState.*
import xyz.atrius.waystones.data.WarpState

val Block.powerBlock: Block?
    get() = world.getBlockAt(location.DOWN).takeIf {
        it.type in listOf(Material.RESPAWN_ANCHOR, Material.COMMAND_BLOCK, Material.OBSIDIAN)
    }

val Block.isPowered: Boolean
    get() = !isInhibited() && (hasInfinitePower() || hasNormalPower())

fun Block.hasNormalPower(): Boolean =
    ((powerBlock?.blockData as? RespawnAnchor)?.charges ?: 0) > 0

fun Block.isWaystone(): Boolean =
    type == Material.LODESTONE

fun Block.hasPower(player: Player): Boolean = when(configuration.requirePower()) {
    INTER_DIMENSION -> location.sameDimension(player.location) || isPowered
    ALL -> isPowered
    else -> false
}

fun Block.isInhibited(): Boolean =
    powerBlock?.type == Material.OBSIDIAN

fun Block.hasInfinitePower(): Boolean =
    powerBlock?.type == Material.COMMAND_BLOCK

fun Block.getWarpState(player: Player): WarpState = when {
    !isWaystone() -> None
    isInhibited() -> Inhibited
    !hasPower(player) -> Unpowered
    !location.isSafe -> Obstructed
    hasInfinitePower() -> Infinite
    else -> Active(location.range())
}

fun Beacon.isActive(): Boolean {
    return tier > 0 && ((y + 1)..world.maxHeight).all {
        !world.getBlockAt(x, it, z).type.isSolid
    }
}
