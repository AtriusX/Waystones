package xyz.atrius.waystones

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.koin.core.Koin
import org.koin.ksp.generated.module
import xyz.atrius.waystones.command.waystones.WaystoneCommand
import xyz.atrius.waystones.config.PluginModule
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.config.property.EnableAdvancementsProperty
import xyz.atrius.waystones.data.config.property.EnableKeyItemsProperty
import xyz.atrius.waystones.data.config.property.LocaleProperty
import xyz.atrius.waystones.data.crafting.CompassRecipe
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.manager.AdvancementManager
import xyz.atrius.waystones.manager.EventManager
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.service.WorldRatioService
import xyz.atrius.waystones.utility.registerRecipes

lateinit var localization : Localization

@Suppress("unused")
class Waystones : KotlinPlugin(PluginModule.module) {

    override fun enable(koin: Koin) {
        localization  = Localization(this, koin.get<LocaleProperty>())
        // Load services
        val warpNameService = koin.get<WarpNameService>()

        warpNameService.load()

        val worldRatioService = koin.get<WorldRatioService>()

        worldRatioService.load()
        // Initialize events
        koin.get<EventManager>().initialize()
        // Register warp key recipe if enabled
        if (koin.get<EnableKeyItemsProperty>().value) {
            logger.info("Loading recipes!")
            registerRecipes(
                koin.get<CompassRecipe>()
            )
        }
        // Register plugin advancements
        if (koin.get<EnableAdvancementsProperty>().value) {
            val advancementManager = koin.get<AdvancementManager>()
            advancementManager.loadAdvancements()
        }

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {
            it.registrar().register(koin.get<WaystoneCommand>().create(), "Test")
        }

        logger.info("Waystones loaded!")
    }

    override fun disable(koin: Koin) {
        logger.info("Waystones disabled!")
    }
}
