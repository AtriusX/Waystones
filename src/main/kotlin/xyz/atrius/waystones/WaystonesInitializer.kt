package xyz.atrius.waystones

import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.data.config.property.EnableAdvancementsProperty
import xyz.atrius.waystones.data.config.property.EnableKeyItemsProperty
import xyz.atrius.waystones.internal.PluginInitializer
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.manager.CommandManager
import xyz.atrius.waystones.manager.ConfigManager
import xyz.atrius.waystones.manager.CraftingRecipeManager
import xyz.atrius.waystones.manager.EventManager
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.service.WorldRatioService

@Single
class WaystonesInitializer(
    private val configManager: ConfigManager,
    private val warpNameService: WarpNameService,
    private val worldRatioService: WorldRatioService,
    private val eventManager: EventManager,
    private val craftingRecipeManager: CraftingRecipeManager,
    private val advancementManager: AdvancementManager,
    private val enableKeyItemsProperty: EnableKeyItemsProperty,
    private val enableAdvancementsProperty: EnableAdvancementsProperty,
    private val commandManager: CommandManager,
) : PluginInitializer {

    override fun enable() {
        configManager.load()
        // Load services
        warpNameService.load()
        worldRatioService.load()
        // Initialize events
        eventManager.initialize()
        // Register warp key recipe if enabled
        if (enableKeyItemsProperty.value) {
            craftingRecipeManager.load()
        }
        // Register plugin advancements
        if (enableAdvancementsProperty.value) {
            advancementManager.loadAdvancements()
        }

        commandManager.register()

        logger.info("Waystones loaded!")
    }

    override fun disable() {
        logger.info("Waystones disabled!")
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(WaystonesInitializer::class.java)
    }
}