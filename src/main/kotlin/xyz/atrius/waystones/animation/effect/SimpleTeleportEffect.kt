package xyz.atrius.waystones.animation.effect

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.handler.WaystoneHandler
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.*
import kotlin.math.ceil

class SimpleTeleportEffect(warp: WaystoneHandler) : TeleportEffect {
    private val player = warp.player
    private val location = player.location
    private val name = warp.name

    override fun start() {
        location.playSound(Sound.BLOCK_PORTAL_AMBIENT, pitch = 0f)
    }

    override fun animation(timer: Long) {
        val seconds = ceil(timer / 20.0).toInt() // TODO: Should this be moved elsewhere?
        player.sendActionMessage(localization["warp-wait", name, seconds])
        // Play warp animation if enabled
        if (!configuration.warpAnimations())
            return
        val amp = configuration.waitTime() - timer + 0.1
        val ratio = timer * 2 + 1
        val period = (System.currentTimeMillis() / 3).toDouble()
        val world  = player.world
        world.forceParticle(Particle.ASH, player.location.rotateY(period, timer.toDouble() / amp), 200)
        world.forceParticle(
            Particle.LARGE_SMOKE, location, 250 / ratio.toInt(), 0.2, 0.5, 0.2, 0.0
        )
    }

    override fun end() {
        player.stopSound(Sound.BLOCK_PORTAL_AMBIENT)
    }

    override fun endAtLocation(location: Location) {
        player.world.forceParticle(
            Particle.LARGE_SMOKE, location.UP.center, 400, 0.2, 0.5, 0.2, 0.1
        )
        // Warp sound effects
        location.playSound(Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
        location.playSound(Sound.BLOCK_BELL_RESONATE, 20f, 0f)
        location.playSound(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE)
    }

    override fun cancel() = Unit
}
