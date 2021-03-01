package xyz.atrius.waystones.animation

import org.bukkit.Bukkit
import org.bukkit.Location
import xyz.atrius.waystones.animation.effect.TeleportEffect
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.utility.scheduleRepeatingAutoCancelTask

object AnimationManager {
    private val scheduler  = Bukkit.getScheduler()
    private val animations = hashMapOf<Int, TeleportEffect>()

    fun register(effect: TeleportEffect, to: Location, onComplete: () -> Unit = {}) = effect.run {
        effect.start()
        scheduler.scheduleRepeatingAutoCancelTask(configuration.waitTime().toLong(), 1, { animation(it) }) {
            onComplete()
            end()
            // Preload chunk (hopefully this plays the end animation)
            to.world?.getChunkAt(to)
            endAtLocation(to)
            onComplete()
        }
    }

    fun cancel(id: Int) {
        scheduler.cancelTask(id)
        animations.remove(id)?.cancel()
    }
}