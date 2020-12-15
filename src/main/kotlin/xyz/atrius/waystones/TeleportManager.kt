package xyz.atrius.waystones

import org.bukkit.entity.Player
import xyz.atrius.waystones.animation.AnimationManager
import xyz.atrius.waystones.animation.effect.SimpleTeleportEffect
import xyz.atrius.waystones.handler.WaystoneHandler

object TeleportManager {
    private val queuedTeleports = HashMap<Player, Int>()

    fun queueTeleport(player: Player, warp: WaystoneHandler, onComplete: () -> Unit = {}) {
        // Cancel any previous queues for this player
        if (contains(player))
            cancel(player)
        // Queue the task and store the task id for if we need to cancel sooner
        queuedTeleports[player] =
            AnimationManager.register(SimpleTeleportEffect(warp), warp.warpLocation, onComplete)
    }

    fun cancel(player: Player) {
        val id = queuedTeleports.remove(player) ?: -1
        AnimationManager.cancel(id)
    }

    operator fun contains(player: Player) =
            player in queuedTeleports
}

