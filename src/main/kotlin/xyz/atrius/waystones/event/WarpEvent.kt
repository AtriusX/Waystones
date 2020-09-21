package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
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
import xyz.atrius.waystones.Power.ALL
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*
import kotlin.math.ceil
import kotlin.math.round

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
            if (meta.isLodestoneTracked) return player.sendActionError(
                "The link to this warpstone has been severed"
            )
        }
        val location       = meta.lodestone ?: return
        val playerLocation = player.location
        val interDimension = !location.sameDimension(playerLocation)
        val name           = names[location] ?: "Warpstone"
        // Prevent travel between worlds if world jumping is disabled
        if (!config.jumpWorlds && interDimension) return player.sendActionError(
            "Cannot locate $name due to dimensional interference"
        )
        // Check the power requirements
        when (config.requirePower) {
            ALL -> if (!location.isPowered) return player.sendActionError(
                "$name does not currently have power"
            )
            INTER_DIMENSION ->  if (interDimension && !location.isPowered) return player.sendActionError(
                "$name does not currently have power"
            )
            else -> Unit
        }
        // Block the warp if teleportation is not safe
        if (!location.isSafe) return player.sendActionError(
            "$name is obstructed and cannot be used."
        )
        // Extra conditions if the plugin has set a distance limit
        if (config.limitDistance) {
            // Get the distance to the warpstone and the stone's range
            val ratio    = if (interDimension) config.worldRatio else 1
            val distance = playerLocation.toVector().distance(location.toVector()) / ratio
            val range    = location.range(config)
            // Prevent warping if the distance is too great
            if (distance > range) return player.sendActionError(
                "$name is out of warp range [${round(distance - range).toInt()} block(s)]"
            )
        }
        // Cancel the event and any previous incomplete tasks
        event.isCancelled = true
        if (queuedTeleports[player] != null)
            scheduler.cancelTask(queuedTeleports[player] ?: -1)
        player.playSound(Sound.BLOCK_PORTAL_AMBIENT, pitch = 0f)
        // This code will run for the duration of the timer period
        val wait = { timer: Long ->
            val seconds = ceil(timer / 20.0).toInt()
            val period  = (System.currentTimeMillis() / 3).toDouble()
            player.run {
                world.spawnParticle(
                    Particle.ASH, this.location.rotateY(period), 50
                )
                sendActionMessage("Warping to $name in $seconds second(s)", ChatColor.GREEN)
                if (timer < 7) world.spawnParticle(
                    Particle.SMOKE_LARGE, this.location.UP, 100, 0.2, 0.5, 0.2, 0.0
                )
            }
        }
        // This code will run at the end of the timer
        val finish = Runnable {
            player.run {
                stopSound(Sound.BLOCK_PORTAL_AMBIENT)
                sendActionMessage("Warped to $name", ChatColor.GOLD)
                teleport(location.UP.center.also {
                    it.yaw   = this.location.yaw
                    it.pitch = this.location.pitch
                })
                playSound(Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
                playSound(Sound.BLOCK_BELL_RESONATE, 20f, 0f)
            }
            scheduler.cancelTask(queuedTeleports.remove(player) ?: -1)
            val powerBlock = location.powerBlock ?: return@Runnable
            when (config.requirePower) {
                ALL             -> deplete(player, powerBlock)
                INTER_DIMENSION -> if (interDimension) deplete(player, powerBlock)
                else            -> Unit
            }
            scheduler.scheduleSyncDelayedTask(plugin, {
                player.sendActionMessage("You feel a chill in your bones...", ChatColor.DARK_GRAY)
            }, 80)
        }
        // Queue the task and store the task id if we need to cancel sooner
        queuedTeleports[player] = scheduler.scheduleRepeatingAutoCancelTask(plugin, 1, config.waitTime.toLong(), wait, finish)
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        if (!event.hasMovedBlock())
            return
        val player = event.player
        if (player in queuedTeleports) {
            scheduler.cancelTask(queuedTeleports.remove(event.player) ?: return)
            player.sendActionMessage("")
        }
    }

    private fun deplete(player: Player, powerBlock: Block) = powerBlock.update<RespawnAnchor> {
        player.playSound(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE)
        charges--
    }
}