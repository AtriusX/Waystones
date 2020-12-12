package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.persistence.PersistentDataType.INTEGER
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.KeyState
import xyz.atrius.waystones.data.KeyState.*
import xyz.atrius.waystones.plugin

operator fun ItemMeta.get(key: String) =
        persistentDataContainer.get(NamespacedKey(plugin, key), INTEGER)

operator fun <T> ItemMeta.set(key: String, type: PersistentDataType<T, T>, value: T) =
        persistentDataContainer.set(NamespacedKey(plugin, key), type, value)

fun ItemStack.isWarpKey() = if (configuration.keyItems)
    itemMeta?.get("is_warp_key") == 1 else type == Material.COMPASS

fun CompassMeta.isSevered(): Boolean =
    !hasLodestone() && isLodestoneTracked

fun ItemStack.getKeyState(player: Player): KeyState {
    val meta = itemMeta as? CompassMeta
    val lodestone = meta?.lodestone ?: return None
    return when {
        !isWarpKey() -> None
        meta.isSevered() -> Severed
        player.canWarp() -> Blocked
        else -> Connected(lodestone)
    }
}