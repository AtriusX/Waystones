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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.atrius.waystones.Power.ALL
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.SicknessOption.DAMAGE_ON_TELEPORT
import xyz.atrius.waystones.configuration
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.*
import xyz.atrius.waystones.utility.WarpState.*
import kotlin.math.ceil
import kotlin.math.round
import kotlin.random.Random

class WarpEvent(private val names : WarpNameService) : Listener {
    private val queuedTeleports = HashMap<Player, Int>()
    private val scheduler       = Bukkit.getScheduler()

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        // Don't start warp while flying with elytra, not right-clicking, or a lodestone was clicked
        if (player.isGliding
            || event.action !in listOf(RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK)
            || event.clickedBlock?.type == Material.LODESTONE
        ) return
        // Get the item that was used in the event
        val inv  = player.inventory
        val item = inv.itemInMainHand.takeIf {
            event.hand == EquipmentSlot.HAND || it.type == Material.COMPASS
        } ?: inv.itemInOffHand
        // Determine the state of the warp key
        val keyState = item.getKeyState(player)
        val keyError = when (keyState) {
            is KeyState.Blocked -> "You are too sick to warp"
            is KeyState.Severed -> "The link to this warpstone has been severed"
            else                -> null
        }
        keyError?.let {
            return player.sendActionError(it)
        }
        // Determine the state of the warpstone
        val location       = (keyState as? KeyState.Connected)?.warp ?: return
        val playerLocation = player.location
        val interDimension = !location.sameDimension(playerLocation)
        val name           = names[location] ?: "Warpstone"
        val block          = location.block
        val state          = block.getWarpState(player)
        val warpError      = when {
            state is Unpowered  ->
                "$name does not currently have power"
            state is Obstructed ->
                "$name is obstructed and cannot be used"
            state is InterDimension && !configuration.jumpWorlds ->
                "Cannot locate $name due to dimensional interference"
            state is Functional && configuration.limitDistance -> {
                // Calculate range and distance from warp
                val range    = state.range / if (interDimension) configuration.worldRatio else 1
                val distance = playerLocation.toVector().distance(location.toVector())
                if (distance > range)
                    "$name is out of warp range [${round(distance - range).toInt()} block(s)]"
                else null
            }
            else -> null
        }
        warpError?.let {
            return player.sendActionError(it)
        }
        // Cancel any previous queues for this player
        if (queuedTeleports[player] != null)
            scheduler.cancelTask(queuedTeleports[player] ?: -1)
        // Play an ambient effect to initiate the teleport
        playerLocation.playSound(Sound.BLOCK_PORTAL_AMBIENT, pitch = 0f)
        // Queue the task and store the task id for if we need to cancel sooner
        queuedTeleports[player] = scheduler.scheduleRepeatingAutoCancelTask(
            configuration.waitTime.toLong(), wait(player, name),
            finish(player, location, name, block, interDimension, item)
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
            player.sendActionError("Warp cancelled due to movement")
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (!configuration.damageStopsWarping || entity !is Player)
            return
        scheduler.cancelTask(queuedTeleports.remove(entity) ?: -1)
        entity.sendActionMessage("")
    }

    private fun wait(player: Player, name: String) = { timer: Long ->
        val seconds = ceil(timer / 20.0).toInt()
        player.sendActionMessage("Warping to $name in $seconds second(s)", ChatColor.DARK_GREEN)
        // Play warp animation if enabled
        if (configuration.warpAnimations) {
            val amp = configuration.waitTime - timer + 0.1
            val ratio = timer * 2 + 1
            val period = (System.currentTimeMillis() / 3).toDouble()
            val world  = player.world
            world.spawnParticle(Particle.ASH, player.location.rotateY(period, timer.toDouble() / amp), 200)
            world.spawnParticle(
                Particle.SMOKE_LARGE, player.location, 250 / ratio.toInt(), 0.2, 0.5, 0.2, 0.0
            )
        }
    }

    private fun finish(
        player        : Player,
        warpLocation  : Location,
        warpName      : String,
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
            warpLocation.world?.forceSpawnParticle(
                Particle.SMOKE_LARGE, location.UP.center, 400, 0.2, 0.5, 0.2, 0.1
            )
            // Warp sound effects
            warpLocation.playSound(Sound.ENTITY_STRAY_DEATH, 0.5f, 0f)
            warpLocation.playSound(Sound.BLOCK_BELL_RESONATE, 20f, 0f)
            // Skip debuffs if the player is immortal
            if (!immortal) {
                val sick = hasPortalSickness()
                // Damage the player if damage is enabled and they aren't immortal
                if (sick && configuration.portalSickWarping == DAMAGE_ON_TELEPORT)
                    damage(configuration.portalSicknessDamage)
                // Give portal sickness to the player if they aren't immortal, are unlucky, or already are sick
                if ((configuration.portalSickness
                    && (sick || Random.nextDouble() < configuration.portalSicknessChance))
                    && !block.hasInfinitePower()
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
        val power = configuration.requirePower
        if (power == ALL || (interDimension && power == INTER_DIMENSION)) {
            // Only deplete power if the power is not infinite
            if (!block.hasInfinitePower()) block.powerBlock?.update<RespawnAnchor> {
                charges--
            }
            warpLocation.playSound(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE)
        }
        // Destroy the compass if single-use mode is enabled
        if (configuration.singleUse && !player.immortal)
            item.amount--
    }
}

