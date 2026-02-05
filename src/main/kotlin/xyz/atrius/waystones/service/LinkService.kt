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
import org.bukkit.persistence.PersistentDataType
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.RelinkableKeysProperty
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.manager.LocalizedString
import xyz.atrius.waystones.provider.DefaultKeyProvider
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.utility.addItemNaturally
import xyz.atrius.waystones.utility.get
import xyz.atrius.waystones.utility.locationCode
import xyz.atrius.waystones.utility.playSound
import xyz.atrius.waystones.utility.toKey
import xyz.atrius.waystones.utility.update

@Single
class LinkService(
    private val localization: LocalizationManager,
    private val relinkableKeys: RelinkableKeysProperty,
    private val keyService: KeyService,
    private val defaultKeyProvider: DefaultKeyProvider,
    private val waystoneInfoRepository: WaystoneInfoRepository,
) {

    fun process(player: Player, item: ItemStack, block: Block): Either<LinkServiceError, Unit> = either {
        ensure(keyService.isWarpKey(item) && block.type == Material.LODESTONE) {
            LinkServiceError.Ignore
        }

        val meta = ensureNotNull(item.itemMeta as? CompassMeta) {
            LinkServiceError.Ignore
        }
        val info = waystoneInfoRepository
            .getWaystone(block.location)
            .get()
        val metaName = meta["waystone_name", PersistentDataType.STRING]
        val name = info?.name
        // Determine if the key is being relinked to a different waystone
        val isDifferentName = name != null && metaName != name
        val isSameLocation = block.location == meta.lodestone
        // A warp key having an existing waystone name in its metadata implies that the key was already
        // linked once before. If this field is present and differs from the name of the waystone present
        // on the database, and the location is the same, we can allow the key to be relinked to the waystone.
        when (isSameLocation && isDifferentName) {
            true -> updateWarpKey(player, item, block, name)
            else -> linkNewWarpKey(player, item, block, name, meta).bind()
        }
    }

    private fun updateWarpKey(
        player: Player,
        item: ItemStack,
        block: Block,
        name: String?,
    ) {
        item.link(player, block, name)
        player.playSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)
    }

    private fun linkNewWarpKey(
        player: Player,
        item: ItemStack,
        block: Block,
        name: String?,
        meta: CompassMeta,
    ): Either<LinkServiceError, Unit> = either {
        // Check if the key already has a linked location. If relinking is
        // disabled, we should not allow the user to relink the key.
        if (!relinkableKeys.value()) {
            ensure(!meta.hasLodestone()) {
                LinkServiceError.NotRelinkable(localization)
            }
        }
        // Prevent relinking if the location is the same
        ensure(meta.lodestone != block.location) {
            LinkServiceError.AlreadyLinked(localization)
        }
        // Add item to players inventory
        val key = defaultKeyProvider
            .getKey(player)
            .link(player, block, name)
        // Add item and play sound
        player.inventory.addItemNaturally(item, key)
        player.playSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)
    }

    private fun ItemStack.link(player: Player, block: Block, name: String?) = update<CompassMeta> {
        lodestone = block.location
        isLodestoneTracked = true

        if (!name.isNullOrEmpty()) {
            val displayName = localization["key-name"]
                .format(player)
                .let { "$it: ($name)" }
                .let(Component::text)
            this.displayName(displayName)
            // Ensure the item has the waystone name set in its PDC
            // We will need this later for verification if the user attempts to relink the key
            persistentDataContainer.set(
                "waystone_name".toKey(),
                PersistentDataType.STRING,
                name,
            )
        }

        val name = name ?: localization["unnamed-waystone"].format(player)
        val lore = localization["link-key-lore", name, lodestone?.locationCode]
            .format(player)
            .let(Component::text)

        lore(listOf(lore))
    }

    sealed class LinkServiceError(val message: () -> LocalizedString?) {

        object Ignore : LinkServiceError({ null })

        class NotRelinkable(localization: LocalizationManager) :
            LinkServiceError({ localization["link-not-relinkable"] })

        class AlreadyLinked(localization: LocalizationManager) :
            LinkServiceError({ localization["link-already-linked"] })
    }
}
