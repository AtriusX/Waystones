package xyz.atrius.waystones.utility

import org.bukkit.block.Beacon

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
