package xyz.atrius.waystones

import xyz.atrius.waystones.commands.*
import xyz.atrius.waystones.data.advancement.*
import xyz.atrius.waystones.data.config.AdvancementManager
import xyz.atrius.waystones.data.config.Config
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.crafting.CompassRecipe
import xyz.atrius.waystones.event.*
import xyz.atrius.waystones.service.WarpService
import xyz.atrius.waystones.service.WorldRatioService
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.registerEvents
import xyz.atrius.waystones.utility.registerNamespaces
import xyz.atrius.waystones.utility.registerRecipes
import java.util.logging.Logger

lateinit var plugin       : KotlinPlugin
lateinit var configuration: Config
lateinit var localization : Localization
lateinit var log          : Logger

@Suppress("unused")
class Waystones : KotlinPlugin() {

    override fun onEnable() {
        plugin        = this
        configuration = Config(this)
        localization  = Localization(this)
        log           = this.logger
        // Load services
        WarpService.load()
        WorldRatioService.load()
        // Register listeners
        registerEvents(
            PlaceEvent,
            ExplodeEvent,
            SuppressEvent,
            WarpEvent,
            NameEvent,
            DestroyEvent,
            InfoEvent,
            LinkEvent
        )
        // Register warp key recipe if enabled
        if (configuration.keyItems()) {
            log.info("Loading recipes!")
            registerRecipes(
                CompassRecipe
            )
        }
        // Register plugin advancements
        if (configuration.advancements()) {
            log.info("Loading advancements!")
            AdvancementManager.register(
                WAYSTONES,
                SECRET_TUNNEL,
                I_DONT_FEEL_SO_GOOD,
                HEAVY_ARTILLERY,
                CLEAN_ENERGY,
                GIGAWARPS,
                UNLIMITED_POWER,
                QUANTUM_DOMESTICATION,
                BLOCKED,
                SHOOT_THE_MESSENGER
            )
        }
        // Register command namespaces
        registerNamespaces(
            CommandNamespace("waystones").register(
                InfoCommand,
                GetKeyCommand,
                ReloadCommand,
                ConfigCommand,
                RatioCommand
            )
        )
        log.info("Waystones loaded!")
    }

    override fun onDisable() {
        log.info("Waystones disabled!")
    }
}
