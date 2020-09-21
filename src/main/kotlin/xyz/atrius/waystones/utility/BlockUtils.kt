package xyz.atrius.waystones.utility

import org.bukkit.block.Block
import org.bukkit.block.data.BlockData

inline fun <reified T : BlockData> Block.update(scope: (T) -> Unit)  {
    val data = this.blockData as T
    scope(data)
    this.blockData = data
    this.state.update(true)
}