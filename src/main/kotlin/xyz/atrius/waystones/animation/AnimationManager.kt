package xyz.atrius.waystones.animation

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.animation.effect.TeleportEffect
import xyz.atrius.waystones.data.config.property.WaitTimeProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.utility.scheduleRepeatingAutoCancelTask

@Single
class AnimationManager(
    private val waitTime: WaitTimeProperty,
    private val plugin: KotlinPlugin,
) {

    private val scheduler = Bukkit.getScheduler()

    private val animations = hashMapOf<Player, TeleportEffect>()

    fun register(effect: TeleportEffect, player: Player, to: Location, onComplete: () -> Unit = {}) = effect.run {
        effect.start()
        animations[player] = effect
        plugin.scheduleRepeatingAutoCancelTask(
            waitTime.value().toLong(),
            1,
            {
                animation(it, it)
            }
        ) {
            end()
            // Preload chunk (hopefully this plays the end animation)
            to.world?.getChunkAt(to)
            endAtLocation(to)
            onComplete()
        }
    }

    fun cancel(id: Int, player: Player) {
        scheduler.cancelTask(id)
        animations.remove(player)?.cancel()
    }
}
