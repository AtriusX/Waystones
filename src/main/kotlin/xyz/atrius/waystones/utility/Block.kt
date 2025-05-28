package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.block.Beacon
import org.bukkit.block.Block

val Block.powerBlock: Block?
    get() = world.getBlockAt(location.DOWN).takeIf {
        it.type in listOf(Material.RESPAWN_ANCHOR, Material.COMMAND_BLOCK, Material.OBSIDIAN)
    }

fun Beacon.isActive(): Boolean {
    if (tier <= 0) {
        return false
    }

    for (dy in y .. world.maxHeight) {
        if (world.getBlockAt(x, dy, z).type.isSolid) {
            return false
        }
    }

    return true
}
