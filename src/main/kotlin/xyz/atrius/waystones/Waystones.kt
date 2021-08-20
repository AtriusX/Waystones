package xyz.atrius.waystones

import xyz.atrius.waystones.commands.*
import xyz.atrius.waystones.data.advancement.SUBSPACE_TUNNEL
import xyz.atrius.waystones.data.config.Config
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.crafting.CompassRecipe
import xyz.atrius.waystones.event.*
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.service.WorldRatioService
import xyz.atrius.waystones.utility.*

lateinit var plugin       : KotlinPlugin
lateinit var configuration: Config
lateinit var localization : Localization

@Suppress("unused")
class Waystones : KotlinPlugin() {

    override fun onEnable() {
        plugin        = this
        configuration = Config(this)
        localization  = Localization(this)
        // Load services
        WarpNameService.load()
        WorldRatioService.load()
        // Register listeners
        registerEvents(
            WarpEvent,
            NameEvent,
            DestroyEvent,
            InfoEvent,
            LinkEvent
        )
        // Register warp key recipe if enabled
        if (configuration.keyItems()) {
            logger.info("Loading recipes!")
            registerRecipes(
                CompassRecipe
            )
        }
        // Register plugin advancements
        if (configuration.advancements()){
            logger.info("Loading advancements!")
            registerAdvancements(
                SUBSPACE_TUNNEL
            )
        }
        // Register command namespaces
        registerNamespaces(
            CommandNamespace("waystones").register(
                InfoCommand,
                GetKeyCommand,
                ReloadCommand,
                ConfigCommand
            )
        )
        logger.info("Waystones loaded!")
    }

    override fun onDisable() {
        logger.info("Waystones disabled!")
    }
}
