package xyz.atrius.waystones.utility

import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import xyz.atrius.waystones.plugin

inline fun <reified T: Enum<T>> valueOfOrDefault(value: String?, default: T): T {
    val enum = enumValues<T>().firstOrNull { it.toString() == value }
    return enum ?: default.also {
        plugin.logger.info("Setting ${T::class.simpleName} value to $default.")
    }
}

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