package xyz.atrius.waystones.event

import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.QUANTUM_DOMESTICATION
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.service.NameService
import xyz.atrius.waystones.utility.awardAdvancement
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.sendActionMessage

@Single
class NameEvent(
    private val localization: Localization,
    private val nameService: NameService,
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != RIGHT_CLICK_BLOCK)
            return
        val player = event.player
        val item = player.inventory.itemInMainHand
        val block = event.clickedBlock
        val name = nameService.process(player, item, block ?: return)
            .fold(
                { return },
                { it }
            )

        player.sendActionMessage(localization["waystone-set-name", name])
        player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f, 2f)
        player.awardAdvancement(QUANTUM_DOMESTICATION)
        event.cancel()
    }
}
