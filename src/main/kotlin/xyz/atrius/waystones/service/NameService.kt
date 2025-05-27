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

@Single
class NameService(
    private val warpNameService: WarpNameService,
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
            item.type == Material.NAME_TAG
                    && block.type == Material.LODESTONE
                    && meta.hasDisplayName()
        ) {
            NameServiceError.Ignore
        }

        val name = PlainTextComponentSerializer
            .plainText()
            .serialize(meta.displayName()!!)

        warpNameService.add(block.location, name)

        if (player.gameMode != GameMode.CREATIVE) {
            item.amount--
        }

        name
    }

    sealed class NameServiceError {

        object Ignore : NameServiceError()
    }
}