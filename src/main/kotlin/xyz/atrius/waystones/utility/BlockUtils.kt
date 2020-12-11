package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import xyz.atrius.waystones.Power.ALL
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.utility.WarpState.*

val Block.powerBlock: Block?
    get() = world.getBlockAt(location.DOWN).takeIf {
        it.type in listOf(Material.RESPAWN_ANCHOR, Material.COMMAND_BLOCK, Material.OBSIDIAN)
    }

val Block.isPowered: Boolean
    get() = !isInhibited() && (hasInfinitePower() || hasNormalPower())

fun Block.isInhibited(): Boolean =
    powerBlock?.type == Material.OBSIDIAN

fun Block.hasInfinitePower(): Boolean =
    powerBlock?.type == Material.COMMAND_BLOCK

fun Block.hasNormalPower(): Boolean =
    (powerBlock?.blockData as? RespawnAnchor)?.charges ?: 0 > 0

fun Block.getWarpState(player: Player): WarpState = when {
    type != Material.LODESTONE               -> None
    configuration.requirePower == ALL
        && !isPowered                        -> Unpowered
    configuration.requirePower == INTER_DIMENSION
        && !location.sameDimension(player.location)
        && !isPowered                        -> Unpowered
    isInhibited()                            -> Inhibited
    hasInfinitePower()                       -> Infinite
    !location.isSafe                         -> Obstructed
    !location.sameDimension(player.location) -> InterDimension(location.range())
    else                                     -> Active(location.range())
}