package xyz.atrius.waystones.utility

import org.bukkit.event.Cancellable
import org.bukkit.event.player.PlayerMoveEvent

fun PlayerMoveEvent.hasMovedBlock() =
    from.toVector().toBlockVector() != to?.toVector()?.toBlockVector()

fun Cancellable.cancel() {
    isCancelled = true
}