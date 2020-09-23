package xyz.atrius.waystones.event

import net.md_5.bungee.api.ChatColor
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_AIR
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.atrius.waystones.Power.ALL
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.SicknessOption.DAMAGE_ON_TELEPORT
import xyz.atrius.waystones.SicknessOption.PREVENT_TELEPORT
import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*
import kotlin.math.ceil
import kotlin.math.round
import kotlin.random.Random

class WarpEvent(
        private val plugin: KotlinPlugin,
        private val names : WarpNameService,
        private val config: Config
) : Listener {
    private val queuedTeleports = HashMap<Player, Int>()
    private val scheduler       = Bukkit.getScheduler()

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        // Player is not able to warp while flying with elytra or if action isn't a right click
        if (player.isGliding || event.action !in listOf(RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK))
            return
        // Check if the player has portal sickness
        if (config.portalSickWarping == PREVENT_TELEPORT && player.hasPortalSickness())
            return player.sendActionError("You are too sick to warp")
        // Get the item that was used in the event
        val inv  = player.inventory
        val item = inv.itemInMainHand.takeIf { event.hand == EquipmentSlot.HAND
            || it.type == Material.COMPASS } ?: inv.itemInOffHand
        // Ignore the event if the item in hand isn't a compass or the clicked block is a lodestone
        if (item.type != Material.COMPASS || event.clickedBlock?.type == Material.LODESTONE)
            return
        // Prevent warping if no stone is connected
        val meta = item.itemMeta as CompassMeta
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
        val block = location.block
        // Check the power requirements
        if (!when (config.requirePower) {
            ALL             -> block.isPowered(player, name)
            INTER_DIMENSION -> if (interDimension) block.isPowered(player, name) else false
            else -> false
        }) return
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
            if (!block.hasInfinitePower() && distance > range) return player.sendActionError(
                "$name is out of warp range [${round(distance - range).toInt()} block(s)]"
            )
        }
        if (queuedTeleports[player] != null)
            scheduler.cancelTask(queuedTeleports[player] ?: -1)
        // Play an ambient effect to initiate the teleport
        player.playSound(Sound.BLOCK_PORTAL_AMBIENT, pitch = 0f)
        // Queue the task and store the task id for if we need to cancel sooner
        queuedTeleports[player] = scheduler.scheduleRepeatingAutoCancelTask(
                plugin, 1, config.waitTime.toLong(), wait(player, name),
                finish(player, name, location, block, interDimension, item)
        )
        event.cancel()
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        if (!event.hasMovedBlock())
            return
        val player = event.player
        if (player in queuedTeleports) {
            scheduler.cancelTask(queuedTeleports.remove(player) ?: return)
            player.sendActionMessage("")
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (!config.damageStopsWarping || entity !is Player)
            return
        scheduler.cancelTask(queuedTeleports.remove(entity) ?: -1)
        entity.sendActionMessage("")
    }

    private fun Block.isPowered(player: Player, name: String): Boolean = if (!isPowered) {
        if (!isInhibited()) player.sendActionError("$name does not currently have power")
        false
    } else true

    private fun wait(player: Player, name: String) = { timer: Long ->
        val seconds = ceil(timer / 20.0).toInt()
        player.sendActionMessage("Warping to $name in $seconds second(s)", ChatColor.DARK_GREEN)
        // Play warp animation if enabled
        if (config.warpAnimations) {
            val period   = (System.currentTimeMillis() / 3).toDouble()
            val world    = player.world
            world.spawnParticle(Particle.ASH, player.location.rotateY(period), 50)
            if (timer < 7) world.spawnParticle(
                    Particle.SMOKE_LARGE, player.location.UP, 100, 0.2, 0.5, 0.2, 0.0
            )
        }
    }

    private fun finish(
        player        : Player,
        warpName      : String,
        warpLocation  : Location,
        block         : Block,
        interDimension: Boolean,
        item          : ItemStack
    ): () -> Unit = {
        player.run {
            queuedTeleports.remove(this)
            stopSound(Sound.BLOCK_PORTAL_AMBIENT)
            // Teleport and notify the player
            teleport(warpLocation.UP.center.also {
                it.yaw   = location.yaw
                it.pitch = location.pitch
            })
            // Warp sound effects
            playSound(Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
            playSound(Sound.BLOCK_BELL_RESONATE, 20f, 0f)
            // Skip debuffs if the player is immortal
            if (!immortal) {
                val sick = hasPortalSickness()
                // Damage the player if damage is enabled and they aren't immortal
                if (sick && config.portalSickWarping == DAMAGE_ON_TELEPORT)
                    damage(config.portalSicknessDamage)
                // Give portal sickness to the player if they aren't immortal, are unlucky, or already are sick
                if (config.portalSickness && !block.hasInfinitePower()
                    && (Random.nextDouble() < config.portalSicknessChance || sick)
                ) {
                    addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, 600, 9))
                    addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 100, 9))
                    sendActionMessage("You feel a chill in your bones...", ChatColor.DARK_GRAY)
                }
            }
            // Display warp message only if user does not get sick
            if (!hasPortalSickness())
                sendActionMessage("Warped to $warpName", ChatColor.DARK_GREEN)
        }
        // Determine how power is depleted from the warp
        when (config.requirePower) {
            ALL             -> deplete(player, block)
            INTER_DIMENSION -> if (interDimension) deplete(player, block)
            else            -> Unit
        }
        // Destroy the compass if single-use mode is enabled
        if (config.singleUse && !player.immortal)
            item.amount--
    }

    private fun deplete(player: Player, warp: Block) {
        // Only deplete power if the power is not infinite
        if (!warp.hasInfinitePower())
            warp.powerBlock?.update<RespawnAnchor> { charges-- }
        player.playSound(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE)
    }
}

