package xyz.atrius.waystones.animation.effect

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
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
import kotlin.math.ceil

class SimpleTeleportEffect(
    private val warp: WaystoneService.Warp,
    private val warpAnimations: WarpAnimationsProperty,
    private val waitTime: WaitTimeProperty,
    private val localization: LocalizationManager,
) : TeleportEffect {

    private val bar = BossBar.bossBar(
        Component.text("Wait Time"),
        1f,
        BossBar.Color.RED,
        BossBar.Overlay.PROGRESS
    )
    private val bossBarColors = listOf(
        BossBar.Color.GREEN,
        BossBar.Color.RED,
        BossBar.Color.RED,
    )
    private val textColors = listOf(
        NamedTextColor.GREEN,
        NamedTextColor.RED,
        NamedTextColor.DARK_RED,
    )

    override fun start() {
        warp.player.location.playSound(Sound.BLOCK_PORTAL_AMBIENT, pitch = 0f)
        warp.player.showBossBar(bar)
    }

    override fun animation(timer: Long, max: Long) {
        val player = warp.player
        val seconds = ceil(timer / 20.0).toInt()
        val progress = timer.toFloat() / waitTime.value().toFloat()
        val color = (progress * bossBarColors.size)
            .toInt()
            .coerceIn(0, bossBarColors.size - 1)
        val waitMessage = localization["warp-wait", warp.name, seconds]
            .format(player)
            .let(Component::text)
            .color(textColors[color])

        bar
            .name(waitMessage)
            .progress(progress)
            .color(bossBarColors[color])
        // Play warp animation if enabled
        if (!warpAnimations.value()) {
            return
        }

        val amp = waitTime.value() - timer + 0.1
        val ratio = timer * 2 + 1
        val period = (System.currentTimeMillis() / 3).toDouble()
        val world = player.world

        world.forceParticle(
            particle = Particle.ASH,
            location = player.location.rotateY(period, timer.toDouble() / amp),
            count = 200
        )
        world.forceParticle(
            particle = Particle.LARGE_SMOKE,
            location = player.location,
            count = 250 / ratio.toInt(),
            offsetX = 0.2,
            offsetY = 0.5,
            offsetZ = 0.2,
            extra = 0.0
        )
    }

    override fun end() {
        warp.player.stopSound(Sound.BLOCK_PORTAL_AMBIENT)
        warp.player.hideBossBar(bar)
    }

    override fun endAtLocation(location: Location) {
        warp.player.world.forceParticle(
            particle = Particle.LARGE_SMOKE,
            location = location.UP.center,
            count = 400,
            offsetX = 0.2,
            offsetY = 0.5,
            offsetZ = 0.2,
            extra = 0.1
        )
        // Warp sound effects
        location.playSound(Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
        location.playSound(Sound.BLOCK_BELL_RESONATE, 20f, 0f)
        location.playSound(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE)
    }

    override fun cancel() {
        val player = warp.player
        player.hideBossBar(bar)
        player.stopSound(Sound.BLOCK_PORTAL_AMBIENT)
        player.location.playSound(Sound.BLOCK_BEACON_DEACTIVATE, 20f, 0f)
    }
}
