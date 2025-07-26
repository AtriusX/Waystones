package xyz.atrius.waystones.provider

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType.INTEGER
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.utility.set
import xyz.atrius.waystones.utility.update

@Single
class DefaultKeyProvider(
    private val localization: LocalizationManager,
) {

    fun getKey(
        target: Player? = null,
        amount: Int = 1,
    ): ItemStack = ItemStack(Material.COMPASS, amount).update<CompassMeta> {
        this["is_warp_key", INTEGER] = 1
        val lore = localization["key-lore"]
            .format(target)
            .let(Component::text)
        val name = localization["key-name"]
            .format(target)
            .let(Component::text)

        setRarity(ItemRarity.UNCOMMON)
        lore(listOf(lore))
        displayName(name)
    }
}
