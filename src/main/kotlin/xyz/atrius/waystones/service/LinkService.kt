package xyz.atrius.waystones.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.LocalizedString
import xyz.atrius.waystones.data.config.property.RelinkableKeysProperty
import xyz.atrius.waystones.data.crafting.defaultWarpKey
import xyz.atrius.waystones.utility.*

@Single
class LinkService(
    private val localization: Localization,
    private val relinkableKeys: RelinkableKeysProperty,
    private val warpNameService: WarpNameService,
    private val keyService: KeyService,
) {

    fun process(player: Player, item: ItemStack, block: Block): Either<LinkServiceError, Unit> = either {
        ensure(keyService.isWarpKey(item) && block.type == Material.LODESTONE) {
            LinkServiceError.Ignore
        }

        val meta = ensureNotNull(item.itemMeta as? CompassMeta) {
            LinkServiceError.Ignore
        }

        if (!relinkableKeys.value) {
            ensure(!meta.hasLodestone()) {
                LinkServiceError.NotRelinkable(localization)
            }
        }
        // Prevent relinking if the location is the same
        ensure(meta.lodestone != block.location) {
            LinkServiceError.AlreadyLinked(localization)
        }
        // Add item to players inventory
        val key = defaultWarpKey(localization).link(block)
        // Add item and play sound
        player.inventory.addItemNaturally(item, key)
        player.playSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)
    }

    private fun ItemStack.link(block: Block) = update<CompassMeta> {
        lodestone = block.location
        isLodestoneTracked = true

        val name = warpNameService[block.location]
            ?: localization["unnamed-waystone"]

        lore(
            listOf(
                Component.text(
                    localization["link-key-lore", name, lodestone?.locationCode].format()
                )
            )
        )
    }

    sealed class LinkServiceError(val message: () -> LocalizedString?) {

        object Ignore : LinkServiceError({ null })

        class NotRelinkable(localization: Localization) :
            LinkServiceError({ localization["link-not-relinkable"] })

        class AlreadyLinked(localization: Localization) :
            LinkServiceError({ localization["link-already-linked"] })
    }
}