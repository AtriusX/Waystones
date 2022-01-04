package xyz.atrius.waystones.handler

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import xyz.atrius.waystones.data.Waystone
import xyz.atrius.waystones.handler.HandleState.Ignore
import xyz.atrius.waystones.handler.HandleState.Success
import xyz.atrius.waystones.service.WarpService

class NameHandler(
        override val player: Player,
        private val item: ItemStack,
        private val block: Block
) : PlayerHandler {
    private val meta = item.itemMeta

    override fun handle(): HandleState = when {
        item.type  != Material.NAME_TAG  ||
        block.type != Material.LODESTONE ||
        meta?.hasDisplayName() != true   -> Ignore
        else -> Success
    }

    fun createName(): String? {
        if (meta == null)
            return null
        val name = meta.displayName
        WarpService.add(block.location, Waystone(name))
        if (player.gameMode != GameMode.CREATIVE)
            item.amount--
        return name
    }
}