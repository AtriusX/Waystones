package xyz.atrius.waystones.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.dao.WaystoneInfo
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.utility.locationCode

@Single
class NameService(
    private val waystoneInfoRepository: WaystoneInfoRepository,
) {

    fun process(
        player: Player,
        item: ItemStack,
        block: Block,
    ): Either<NameServiceError, String> = either {
        val meta = ensureNotNull(item.itemMeta) {
            NameServiceError.Ignore
        }

        ensure(
            item.type == Material.NAME_TAG &&
                block.type == Material.LODESTONE &&
                meta.hasDisplayName()
        ) {
            NameServiceError.Ignore
        }

        val displayName = meta.displayName()

        ensureNotNull(displayName) {
            NameServiceError.Ignore
        }

        val name = PlainTextComponentSerializer
            .plainText()
            .serialize(displayName)
        val info = waystoneInfoRepository
            .getWaystone(block.location)
            .get()

        ensure(name != info?.name) {
            logger.debug("Waystone at ${block.location.locationCode} already has name '$name'! Skipping rename.")
            NameServiceError.Ignore
        }

        val newInfo = WaystoneInfo
            .fromLocation(block.location, name)
        waystoneInfoRepository.save(newInfo)

        if (player.gameMode != GameMode.CREATIVE) {
            item.amount--
        }

        name
    }

    sealed class NameServiceError {

        object Ignore : NameServiceError()
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(NameService::class.java.name)
    }
}
