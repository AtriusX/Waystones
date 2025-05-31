package xyz.atrius.waystones.utility

import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

inline fun <reified T : BlockData> Block.update(scope: T.() -> Unit) = also {
    val data = this.blockData as T
    scope(data)
    this.blockData = data
    this.state.update(true)
}

inline fun <reified T : ItemMeta> ItemStack.update(scope: T.() -> Unit) = also {
    val data = this.itemMeta as T
    scope(data)
    this.itemMeta = data
}
