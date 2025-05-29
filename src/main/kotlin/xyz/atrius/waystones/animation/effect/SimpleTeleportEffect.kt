package xyz.atrius.waystones.animation.effect

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import xyz.atrius.waystones.data.config.property.WaitTimeProperty
import xyz.atrius.waystones.data.config.property.WarpAnimationsProperty
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.service.WaystoneService
import xyz.atrius.waystones.utility.UP
import xyz.atrius.waystones.utility.center
import xyz.atrius.waystones.utility.forceParticle
import xyz.atrius.waystones.utility.playSound
import xyz.atrius.waystones.utility.rotateY
import xyz.atrius.waystones.utility.sendActionMessage
import kotlin.math.ceil

class SimpleTeleportEffect(
    private val warp: WaystoneService.Warp,
    private val warpAnimations: WarpAnimationsProperty,
    private val waitTime: WaitTimeProperty,
    private val localization: LocalizationManager,
) : TeleportEffect {

    override fun start() {
        warp.player.location.playSound(Sound.BLOCK_PORTAL_AMBIENT, pitch = 0f)
    }

    override fun animation(timer: Long) {
        val player = warp.player
        val seconds = ceil(timer / 20.0).toInt()

        player.sendActionMessage(localization["warp-wait", warp.name, seconds])
        // Play warp animation if enabled
        if (!warpAnimations.value()) {
            return
        }

        val amp = waitTime.value() - timer + 0.1
        val ratio = timer * 2 + 1
        val period = (System.currentTimeMillis() / 3).toDouble()
        val world = player.world

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
            Particle.LARGE_SMOKE,
            location.UP.center,
            400,
            0.2,
            0.5,
            0.2,
            0.1
        )
        // Warp sound effects
        location.playSound(Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
        location.playSound(Sound.BLOCK_BELL_RESONATE, 20f, 0f)
        location.playSound(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE)
    }

    override fun cancel() = Unit
}
