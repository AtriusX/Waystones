package xyz.atrius.waystones.utility

import arrow.core.Either
import org.bukkit.entity.Player
import xyz.atrius.waystones.manager.LocalizedString

/**
 * Sends a localized error message to the player when the [Either] represents a failure.
 *
 * This is a convenience for handling error cases in event handlers where the Left side
 * of an Either contains an error type that can be mapped to a [LocalizedString].
 * The Right (success) case is ignored.
 */
fun <L, R> Either<L, R>.sendError(
    player: Player,
    errorMapper: (L) -> LocalizedString?
) {
    this.onLeft { error ->
        errorMapper(error)?.let { msg ->
            player.sendActionError(msg.format(player))
        }
    }
}
