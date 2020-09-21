package xyz.atrius.waystones.event

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_AIR
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.util.Vector
import xyz.atrius.waystones.Power.ALL
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

class WarpEvent(
        private val plugin: KotlinPlugin,
        private val names : WarpNameService,
        private val config: Config
) : Listener {
    private val queuedTeleports = HashMap<Player, Int>()
    private val scheduler       = Bukkit.getScheduler()

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        // Ignore any non-right-click actions
        val action = event.action
        if (action != RIGHT_CLICK_BLOCK && action != RIGHT_CLICK_AIR)
            return
        val player = event.player
        val item   = player.inventory.itemInMainHand
        // Ignore the event if the item in hand isn't a compass or the clicked block is a lodestone
        if (item.type != Material.COMPASS || event.clickedBlock?.type == Material.LODESTONE)
            return
        val meta = item.itemMeta as CompassMeta
        // Prevent warping if no stone is connected
        if (!meta.hasLodestone()) {
            // Tell the player if the stone was destroyed
            if (meta.isLodestoneTracked)
                player.sendActionError("The link to this warpstone has been severed")
            return
        }
        val location       = meta.lodestone ?: return
        val interDimension = location.world?.name != player.location.world?.name ?: false
        val name           = names[location] ?: "Warpstone"
        // Prevent travel between worlds if world jumping is disabled
        if (!config.jumpWorlds && interDimension) {
            player.sendActionError("Cannot locate $name due to dimensional interference")
            return
        }
        // Check the power requirements
        when (config.requirePower) {
            ALL -> {
                if (!location.isPowered) {
                    player.sendActionError("$name does not currently have power")
                    return
                }
            }
            INTER_DIMENSION -> {
                if (interDimension && !location.isPowered) {
                    player.sendActionError("$name does not currently have power")
                    return
                }
            }
            else -> Unit
        }
        // Block the warp if teleportation is not safe
        if (!location.isSafe) {
            player.sendActionError("$name is obstructed and cannot be used.")
            return
        }
        // Extra conditions if the plugin has set a distance limit
        if (config.limitDistance) {
            // Get the distance to the warpstone and the stone's range
            val ratio    = if (interDimension) config.worldRatio else 1
            val distance = player.location.toVector().distance(location.toVector()) / ratio
            val range    = location.range(config)
            // Prevent warping if the distance is too great
            if (distance > range) {
                player.sendActionError(
                    "$name is out of warp range [${round(distance - range).toInt()} block(s)]"
                )
                return
            }
        }
        // Cancel the event and any previous incomplete tasks
        event.isCancelled = true
        if (queuedTeleports[player] != null)
            scheduler.cancelTask(queuedTeleports[player] ?: -1)
        // This code will run for the duration of the timer period
        var timer = config.waitTime
        val wait = {
            val time = (System.currentTimeMillis() / 3).toDouble()
            player.run {
                world.spawnParticle(
                    Particle.ASH, player.location.add(Vector(cos(time), 2.0, sin(time))), 50
                )
                sendActionMessage("Warping to $name in ${ceil(timer-- / 20.0).toInt()} second(s)", "#00FF00")
                playSound(player.location, Sound.BLOCK_PORTAL_AMBIENT, 1f, 0.3f)
            }
        }
        // This code will run at the end of the timer
        val finish = Runnable {
            player.run {
                stopSound(Sound.BLOCK_PORTAL_AMBIENT)
                sendActionMessage("Warped to $name", "#FF6600")
                teleport(location.UP.center)
                playSound(player.location, Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
                playSound(player.location, Sound.BLOCK_BELL_RESONATE, 10f, 0f)
            }
            scheduler.cancelTask(queuedTeleports[player] ?: -1)
            val powerBlock = location.powerBlock ?: return@Runnable
            when (config.requirePower) {
                ALL             -> deplete(player, powerBlock)
                INTER_DIMENSION -> if (interDimension) deplete(player, powerBlock)
                else            -> Unit
            }
        }
        // Queue the task and store the task id if we need to cancel sooner
        queuedTeleports[player] = scheduler.scheduleRepeatingAutoCancelTask(plugin, 1, timer.toLong(), wait, finish)
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        if (event.from.toVector().toBlockVector() == event.to?.toVector()?.toBlockVector())
            return
        if (event.player in queuedTeleports) {
            scheduler.cancelTask(queuedTeleports.remove(event.player) ?: return)
            event.player.sendActionMessage("")
        }
    }

    private fun deplete(player: Player, powerBlock: Block) = powerBlock.update<RespawnAnchor> {
        player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 1f)
        it.charges--
    }
}