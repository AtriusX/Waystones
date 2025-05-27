package xyz.atrius.waystones.service

import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.animation.AnimationManager
import xyz.atrius.waystones.animation.effect.SimpleTeleportEffect
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.property.WarpAnimationsProperty

@Single
class TeleportService(
    private val animationManager: AnimationManager,
    private val localization: Localization,
    private val warpAnimations: WarpAnimationsProperty,
) {
    private val queuedTeleports = HashMap<Player, Int>()

    fun queueEvent(warp: WaystoneService.Warp, onComplete: () -> Unit = {}) {
        val player = warp.player
        // Cancel any previous queues for this player
        if (contains(player)) {
            cancel(player)
        }
        // Queue the task and store the task id for if we need to cancel sooner
        queuedTeleports[player] = animationManager.register(
            SimpleTeleportEffect(warp, warpAnimations, localization),
            warp.warpLocation
        ) {
            queuedTeleports.remove(player)
            onComplete()
        }
    }

    fun cancel(player: Player) {
        val id = queuedTeleports.remove(player) ?: -1
        animationManager.cancel(id)
    }

    operator fun contains(player: Player) =
        player in queuedTeleports
}