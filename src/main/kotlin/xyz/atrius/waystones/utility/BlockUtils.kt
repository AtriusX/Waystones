package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.RespawnAnchor

val Block.powerBlock: Block?
    get() = world.getBlockAt(location.DOWN).takeIf {
        it.type in listOf(Material.RESPAWN_ANCHOR, Material.COMMAND_BLOCK, Material.OBSIDIAN)
    }

val Block.isPowered: Boolean
    get() {
        val power = powerBlock ?: return false
        if (isInhibited())
            return false
        if (hasInfinitePower())
            return true
        val meta = power.blockData as RespawnAnchor
        return meta.charges > 0
    }

fun Block.hasInfinitePower(): Boolean =
    powerBlock?.type == Material.COMMAND_BLOCK

fun Block.isInhibited(): Boolean =
    powerBlock?.type == Material.OBSIDIAN

inline fun <reified T : BlockData> Block.update(scope: T.() -> Unit)  {
    val data = this.blockData as T
    scope(data)
    this.blockData = data
    this.state.update(true)
}