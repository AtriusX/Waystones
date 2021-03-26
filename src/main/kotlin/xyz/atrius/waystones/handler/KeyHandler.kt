package xyz.atrius.waystones.handler

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.KeyState
import xyz.atrius.waystones.handler.HandleState.*
import xyz.atrius.waystones.utility.getKeyState
import xyz.atrius.waystones.utility.immortal

class KeyHandler(
    override val player: Player,
    private val event: PlayerInteractEvent
) : PlayerHandler {
    private val inv = player.inventory

    private val item: ItemStack = inv.itemInMainHand.takeIf {
        event.hand == EquipmentSlot.HAND || it.type == Material.COMPASS
    } ?: inv.itemInOffHand

    private val keyState: KeyState = item.getKeyState(player)

    override fun handle(): HandleState {
        return Fail(keyState.message ?: return Success)
    }

    fun getLocation(): Location? =
        if (keyState is KeyState.Connected) keyState.warp else null

    fun useKey() {
        if (configuration.singleUse() && !player.immortal)
            item.amount--
    }
}
