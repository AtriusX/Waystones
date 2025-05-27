package xyz.atrius.waystones.animation.effect

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.property.WarpAnimationsProperty
import xyz.atrius.waystones.service.WaystoneService
import xyz.atrius.waystones.utility.*
import kotlin.math.ceil

class SimpleTeleportEffect(
    private val warp: WaystoneService.Warp,
    private val warpAnimations: WarpAnimationsProperty,
    private val localization: Localization,
) : TeleportEffect {

    override fun start() {
        warp.player.location.playSound(Sound.BLOCK_PORTAL_AMBIENT, pitch = 0f)
    }

    override fun animation(timer: Long) {
        val player = warp.player
        val seconds = ceil(timer / 20.0).toInt() // TODO: Should this be moved elsewhere?

        player.sendActionMessage(localization["warp-wait", warp.name, seconds])
        // Play warp animation if enabled
        if (!warpAnimations.value) {
            return
        }

        val amp = configuration.waitTime() - timer + 0.1
        val ratio = timer * 2 + 1
        val period = (System.currentTimeMillis() / 3).toDouble()
        val world  = player.world

        world.forceParticle(
            Particle.ASH,
            player.location.rotateY(period, timer.toDouble() / amp),
            200
        )
        world.forceParticle(
            Particle.LARGE_SMOKE,
            player.location,
            250 / ratio.toInt(),
            0.2,
            0.5,
            0.2,
            0.0
        )
    }

    override fun end() {
        warp.player.stopSound(Sound.BLOCK_PORTAL_AMBIENT)
    }

    override fun endAtLocation(location: Location) {
        warp.player.world.forceParticle(
            Particle.LARGE_SMOKE, location.UP.center, 400, 0.2, 0.5, 0.2, 0.1
        )
        // Warp sound effects
        location.playSound(Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
        location.playSound(Sound.BLOCK_BELL_RESONATE, 20f, 0f)
        location.playSound(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE)
    }

    override fun cancel() = Unit
}
