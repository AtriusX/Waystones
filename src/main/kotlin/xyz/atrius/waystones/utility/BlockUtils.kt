package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import xyz.atrius.waystones.Power.ALL
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.configuration

val Block.powerBlock: Block?
    get() = world.getBlockAt(location.DOWN).takeIf {
        it.type in listOf(Material.RESPAWN_ANCHOR, Material.COMMAND_BLOCK, Material.OBSIDIAN)
    }

val Block.isPowered: Boolean
    get() = when {
        isInhibited()      -> false
        hasInfinitePower() -> true
        powerBlock?.blockData is RespawnAnchor -> {
            val meta = powerBlock?.blockData as RespawnAnchor
            meta.charges > 0
        }
        else -> false
    }

fun Block.hasInfinitePower(): Boolean =
    powerBlock?.type == Material.COMMAND_BLOCK

fun Block.isInhibited(): Boolean =
    powerBlock?.type == Material.OBSIDIAN

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
    else                                     -> Functional(location.range())
}
