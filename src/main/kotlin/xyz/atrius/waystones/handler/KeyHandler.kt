package xyz.atrius.waystones.handler

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.KeyState
import xyz.atrius.waystones.utility.getKeyState
import xyz.atrius.waystones.utility.immortal

class KeyHandler(
    override val player: Player,
    private val event: PlayerInteractEvent
) : PlayerHandler {
    override var error: String? = null
        private set

    private val inv = player.inventory

    private val item: ItemStack = inv.itemInMainHand.takeIf {
        event.hand == EquipmentSlot.HAND || it.type == Material.COMPASS
    } ?: inv.itemInOffHand

    private val keyState: KeyState = item.getKeyState(player)

    override fun handle(): Boolean {
        error = keyState.message
        return error == null
    }

    fun getLocation(): Location? =
        if (keyState is KeyState.Connected) keyState.warp else null

    fun useKey() {
        if (configuration.singleUse() && !player.immortal)
            item.amount--
    }
}