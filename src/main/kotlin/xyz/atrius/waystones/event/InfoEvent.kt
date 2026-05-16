package xyz.atrius.waystones.event

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.service.KeyService
import xyz.atrius.waystones.service.WaystoneService
import xyz.atrius.waystones.utility.cancel
import xyz.atrius.waystones.utility.runOnMainThread
import xyz.atrius.waystones.utility.sendActionMessage

@Single
class InfoEvent(
    @Provided private val plugin: KotlinPlugin,
    private val localization: LocalizationManager,
    private val keyService: KeyService,
    private val waystoneService: WaystoneService,
    private val waystoneInfoRepository: WaystoneInfoRepository,
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK) {
            return
        }

        val block = event.clickedBlock
        val player = event.player
        val item = event.item
            ?: return
        if (block?.type != Material.LODESTONE || !keyService.isWarpKey(item)) {
            return
        }

        val state = waystoneService
            .getWarpState(player, block)
            ?.message()
            ?.format(player)
            ?: return

        waystoneInfoRepository
            .getWaystone(block.location)
            .thenApplyAsync { it?.name ?: localization["unnamed-waystone"].format(player) }
            .runOnMainThread(plugin) { name ->
                player.sendActionMessage(localization["waystone-info", name, state])
            }

        event.cancel()
    }
}
