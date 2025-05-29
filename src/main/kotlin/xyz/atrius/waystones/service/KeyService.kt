package xyz.atrius.waystones.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.EnableKeyItemsProperty
import xyz.atrius.waystones.data.config.property.PortalSicknessWarpingProperty
import xyz.atrius.waystones.data.config.property.SingleUseProperty
import xyz.atrius.waystones.data.config.property.type.SicknessOption.PREVENT_TELEPORT
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.manager.LocalizedString
import xyz.atrius.waystones.utility.get
import xyz.atrius.waystones.utility.hasPortalSickness
import xyz.atrius.waystones.utility.immortal

@Single
class KeyService(
    private val singleUse: SingleUseProperty,
    private val portalSickWarping: PortalSicknessWarpingProperty,
    private val enableKeyItems: EnableKeyItemsProperty,
    private val localization: LocalizationManager,
) {

    fun process(player: Player, event: PlayerInteractEvent): Either<KeyServiceError, Key> = either {
        val inventory = player.inventory
        val item = inventory.itemInMainHand
            .takeIf { event.hand == EquipmentSlot.HAND || it.type == Material.COMPASS }
            ?: inventory.itemInOffHand
        val lodestoneLocation = validateKey(player, item).bind()

        Key(lodestoneLocation, player, item, singleUse.value)
    }

    fun isWarpKey(key: ItemStack) = when (enableKeyItems.value) {
        true -> key.itemMeta?.get("is_warp_key") == 1
        else -> key.type == Material.COMPASS &&
            (key.itemMeta as? CompassMeta)?.lodestone != null
    }

    private fun validateKey(player: Player, key: ItemStack): Either<KeyServiceError, Location> = either {
        val meta = key.itemMeta as? CompassMeta
        val lodestone = ensureNotNull(meta?.lodestone) {
            KeyServiceError.Ignore
        }

        ensure(isWarpKey(key)) {
            KeyServiceError.Ignore
        }

        ensure(meta.hasLodestone() && meta.isLodestoneTracked) {
            KeyServiceError.Severed(localization)
        }

        ensure(!player.hasPortalSickness() && portalSickWarping.value != PREVENT_TELEPORT) {
            KeyServiceError.Blocked(localization)
        }

        lodestone
    }

    data class Key(
        val location: Location,
        private val player: Player,
        private val keyItem: ItemStack,
        private val singleUse: Boolean,
    ) {

        fun useKey() {
            if (singleUse && !player.immortal) {
                keyItem.amount--
            }
        }
    }

    sealed class KeyServiceError(val message: () -> LocalizedString?) {

        object Ignore : KeyServiceError({ null })

        class Severed(localization: LocalizationManager) : KeyServiceError({ localization["key-severed"] })

        class Blocked(localization: LocalizationManager) : KeyServiceError({ localization["key-blocked"] })
    }
}
