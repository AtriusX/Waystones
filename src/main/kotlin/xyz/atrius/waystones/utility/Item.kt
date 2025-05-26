package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.persistence.PersistentDataType.INTEGER
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.plugin

operator fun ItemMeta.get(key: String) =
    persistentDataContainer.get(NamespacedKey(plugin, key), INTEGER)

operator fun <T> ItemMeta.set(key: String, type: PersistentDataType<T, T>, value: T) =
    persistentDataContainer.set(NamespacedKey(plugin, key), type, value ?: throw IllegalStateException("Value must be provided!"))

fun ItemStack.isWarpKey() = if (configuration.keyItems())
    itemMeta?.get("is_warp_key") == 1 else type == Material.COMPASS