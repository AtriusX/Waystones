package xyz.atrius.waystones.handler

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import xyz.atrius.waystones.service.WarpNameService

class NameHandler(
        override val player: Player,
        private val item: ItemStack,
        private val block: Block,
        private val names: WarpNameService
) : PlayerHandler {
    override val error: String? = null

    private val meta = item.itemMeta

    override fun handle(): Boolean = when {
        item.type != Material.NAME_TAG -> false
        block.type != Material.LODESTONE -> false
        meta?.hasDisplayName() != true -> false
        else -> true
    }

    fun createName(): String? {
        if (meta == null)
            return null
        val name = meta.displayName
        names.add(block.location, name)
        if (player.gameMode != GameMode.CREATIVE)
            item.amount--
        return name
    }
}