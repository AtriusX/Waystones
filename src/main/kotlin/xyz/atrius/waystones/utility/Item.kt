package xyz.atrius.waystones.utility

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.persistence.PersistentDataType.INTEGER

operator fun ItemMeta.get(key: String) =
    persistentDataContainer.get(key.toKey(), INTEGER)

operator fun <T> ItemMeta.set(key: String, type: PersistentDataType<T, T>, value: T) =
    persistentDataContainer
        .set(
            key.toKey(),
            type,
            value
                ?: throw IllegalStateException("Value must be provided!")
        )

fun PlayerInventory.addItemNaturally(original: ItemStack, new: ItemStack) {
    val player = holder as Player
    // Add item to inventory
    if (player.immortal) {
        addItem(new)
        return
    }
    // Update item in slot if only one exists
    if (original.amount == 1 && original.type == new.type) {
        original.itemMeta = new.itemMeta
        return
    }


    if (addItem(new).isNotEmpty()) {
        player.world.dropItem(player.location.UP, new)
    }

    original.amount--
}