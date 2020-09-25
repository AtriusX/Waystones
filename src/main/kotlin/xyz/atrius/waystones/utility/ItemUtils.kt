package xyz.atrius.waystones.utility

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType
import xyz.atrius.waystones.configuration


fun ItemStack.isWarpKey() = if (configuration.keyItems)
    itemMeta?.persistentDataContainer?.get(keyValue(), PersistentDataType.INTEGER) == 1
else type == Material.COMPASS

fun CompassMeta.isSevered(): Boolean =
    !hasLodestone() && isLodestoneTracked

fun ItemStack.getKeyState(player: Player): KeyState {
    val meta = itemMeta as? CompassMeta
    return when {
        meta == null || !isWarpKey() -> KeyState.None
        meta.isSevered()             -> KeyState.Severed
        player.canWarp()             -> KeyState.Blocked
        else                         -> KeyState.Connected(meta.lodestone)
    }
}