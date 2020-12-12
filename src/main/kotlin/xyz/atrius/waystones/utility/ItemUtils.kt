package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType.INTEGER
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.utility.KeyState.*


fun ItemStack.isWarpKey() = if (configuration.keyItems)
    itemMeta?.persistentDataContainer?.get(keyValue(), INTEGER) == 1
else type == Material.COMPASS

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