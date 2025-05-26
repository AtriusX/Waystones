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
import xyz.atrius.waystones.SicknessOption.PREVENT_TELEPORT
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.LocalizedString
import xyz.atrius.waystones.data.config.property.PortalSicknessWarpingProperty
import xyz.atrius.waystones.data.config.property.SingleUseProperty
import xyz.atrius.waystones.utility.hasPortalSickness
import xyz.atrius.waystones.utility.immortal
import xyz.atrius.waystones.utility.isWarpKey

@Single
class KeyService(
    private val singleUse: SingleUseProperty,
    private val portalSickWarping: PortalSicknessWarpingProperty,
    private val localization: Localization,
) {

    fun process(player: Player, event: PlayerInteractEvent): Either<KeyServiceError, Key> = either {
        val inventory = player.inventory
        val item = inventory.itemInMainHand
            .takeIf { event.hand == EquipmentSlot.HAND || it.type == Material.COMPASS }
            ?: inventory.itemInOffHand
        val lodestoneLocation = item
            .validateKey(player)
            .bind()

        Key(lodestoneLocation, player, item, singleUse.value)
    }

    private fun ItemStack.validateKey(player: Player): Either<KeyServiceError, Location> = either {
        val meta = itemMeta as? CompassMeta
        val lodestone = ensureNotNull(meta?.lodestone) {
            KeyServiceError.Ignore
        }

        ensure(isWarpKey()) {
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

        class Severed(localization: Localization) : KeyServiceError({ localization["key-severed"] })

        class Blocked(localization: Localization) : KeyServiceError({ localization["key-blocked"] })
    }
}